package owres.stockcomparer.model.data.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileDatabase {

    private static final Logger LOGGER      = Logger.getLogger(ProfileDatabase.class.getName());
    private static final String PROFILE_DIR = "data/profiles/";

    private final Gson gson;

    public ProfileDatabase() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        new File(PROFILE_DIR).mkdirs();
    }

    public void saveProfile(ProfileRecord record) {
        try (Writer w = new FileWriter(fileFor(record.getEmail()))) {
            gson.toJson(record, w);
            LOGGER.info("Profile saved: " + record.getEmail());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not save profile: " + record.getEmail(), e);
        }
    }

    public ProfileRecord loadProfile(String email) {
        File f = fileFor(email);
        if (!f.exists()) return null;
        try (Reader r = new FileReader(f)) {
            return gson.fromJson(r, ProfileRecord.class);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not load profile: " + email, e);
            return null;
        }
    }

    public ProfileRecord authenticate(String email, String password) {
        ProfileRecord record = loadProfile(email);
        if (record == null || !password.equals(record.getPassword())) return null;
        return record;
    }

    private File fileFor(String email) {
        String safe = email.replace("@", "_at_").replace(".", "_");
        return new File(PROFILE_DIR + safe + ".json");
    }
}