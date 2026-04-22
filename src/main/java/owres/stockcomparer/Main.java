package owres.stockcomparer;

import owres.stockcomparer.model.data.Company;
import owres.stockcomparer.model.data.Stock;
import owres.stockcomparer.model.data.database.ProfileDatabase;
import owres.stockcomparer.model.data.database.ProfileRecord;
import owres.stockcomparer.model.graph.Graph;
import owres.stockcomparer.model.graph.IGraph;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        // --- Graph test ---
        IGraph graph = new Graph();
        System.out.println(graph.getData());

        // --- Profile test ---
        ProfileDatabase profileDb = new ProfileDatabase();
        System.out.println("Writing profiles to: " + new File("data/profiles").getAbsolutePath());

        // Build a full Stock object
        Stock tsla = new Stock("TSLA", "Tesla", new Company("Tesla"));

        // Save profile with full stock object
        ProfileRecord record = new ProfileRecord("alice@example.com", "secret123", tsla);
        profileDb.saveProfile(record);
        System.out.println("Profile saved.");

        // Load back as object
        ProfileRecord loaded = profileDb.loadProfile("alice@example.com");
        if (loaded == null) {
            System.out.println("Profile not found.");
        } else {
            System.out.println("--- Profile Object ---");
            System.out.println("Email:        " + loaded.getEmail());
            System.out.println("Password:     " + loaded.getPassword());
            System.out.println("Stock Symbol: " + loaded.getFirstWatchlistStock().getSymbol());
            System.out.println("Stock Name:   " + loaded.getFirstWatchlistStock().getName());
            System.out.println("Company:      " + loaded.getFirstWatchlistStock().getCompany().getName());
            System.out.println("----------------------");
        }

        // Authenticate
        ProfileRecord auth = profileDb.authenticate("alice@example.com", "secret123");
        System.out.println("Auth: " + (auth != null ? "OK" : "FAILED"));
    }
}