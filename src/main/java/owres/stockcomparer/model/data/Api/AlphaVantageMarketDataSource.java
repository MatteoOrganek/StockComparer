package owres.stockcomparer.model.data.Api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AlphaVantageMarketDataSource implements IMarketDataSource {


    @Override
    public StockPriceDataSource fetchDailyCloses(String symbol, LocalDate from, LocalDate to) throws Exception {
        return null;
    }
}
