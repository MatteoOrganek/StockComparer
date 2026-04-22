package owres.stockcomparer.model.data.database;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import owres.stockcomparer.model.data.Api.StockExchange;
import owres.stockcomparer.model.data.IDataProvider;
import owres.stockcomparer.model.data.PriceEntry;
import owres.stockcomparer.model.data.Stock;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database implements IDataProvider {

    private static final Logger LOGGER   = Logger.getLogger(Database.class.getName());
    private static final String DATA_DIR = "data/prices/";

    private final Gson gson;

    public Database() {
        // Gson needs a custom adapter to handle LocalDateTime
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                                new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                                LocalDateTime.parse(json.getAsString()))
                .setPrettyPrinting()
                .create();

        new File(DATA_DIR).mkdirs();
    }

    @Override
    public List<PriceEntry> getData(Stock stock, LocalDateTime startTime, LocalDateTime endTime) {
        PriceHistoryRecord record = loadRecord(stock.getSymbol());
        if (record == null) return new ArrayList<>();

        List<PriceEntry> result = new ArrayList<>();
        for (PriceEntry e : record.getEntries()) {
            LocalDateTime t = e.getTime();
            if (t != null && !t.isBefore(startTime) && !t.isAfter(endTime)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public Boolean isAvailable(Stock stock) {
        PriceHistoryRecord record = loadRecord(stock.getSymbol());
        return record != null && !record.getEntries().isEmpty();
    }

    @Override
    public StockExchange getExchangeForStock(Stock stock) {
        return null;
    }

    /** Call after a successful API fetch to cache data for offline use. */
    public void saveEntries(Stock stock, List<PriceEntry> entries) {
        PriceHistoryRecord record = new PriceHistoryRecord(
                stock.getSymbol(), LocalDateTime.now(), entries
        );
        saveRecord(stock.getSymbol(), record);
    }

    // --- private helpers ---

    private File fileFor(String symbol) {
        return new File(DATA_DIR + symbol.toUpperCase() + ".json");
    }

    private PriceHistoryRecord loadRecord(String symbol) {
        File f = fileFor(symbol);
        if (!f.exists()) return null;
        try (Reader r = new FileReader(f)) {
            return gson.fromJson(r, PriceHistoryRecord.class);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not read cache for " + symbol, e);
            return null;
        }
    }

    private void saveRecord(String symbol, PriceHistoryRecord record) {
        try (Writer w = new FileWriter(fileFor(symbol))) {
            gson.toJson(record, w);
            LOGGER.info("Saved " + record.getEntries().size() + " entries for " + symbol);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not write cache for " + symbol, e);
        }
    }
}