package me.nbtc.premieremporium.storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.nbtc.sheepwars.SheepWars;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private final Map<String, MongoDatabase> databases = new HashMap<>();
    final String databaseName = SheepWars.getInstance().getConfiguration().getConfig().getString("mongo.dbname");

    public MongoDatabase getDatabase() {
        MongoDatabase database = databases.get(databaseName);
        if (database != null) {
            return database;
        } else {
            throw new RuntimeException("No database found for name: " + databaseName);
        }
    }

    public boolean connect() {
        final boolean isConnected = this.initDatasource();
        if (isConnected) {
            this.checkCollection();
        }
        return isConnected;
    }

    private void checkCollection() {
        MongoDatabase database = getDatabase();
        MongoCollection<Document> collection = database.getCollection("sheepwars");

        collection.createIndex(new Document("uuid", 1));
    }

    public boolean initDatasource() {
        try {
            final String connectionString = SheepWars.getInstance().getConfiguration().getConfig().getString("mongo.connectionString");

            com.mongodb.client.MongoClient mongoClient = com.mongodb.client.MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            databases.put(databaseName, database);

            System.out.println("Connected to database: " + databaseName);
            return true;
        } catch (final Exception exception) {
            System.out.println("Error connecting to MongoDB: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isClosed(String databaseName) {
        // MongoDB does not have a concept of closing databases in the same way SQL does
        return !databases.containsKey(databaseName);
    }

    public void disconnect() {
        if (databases.containsKey(databaseName)) {
            databases.remove(databaseName);
            System.out.println("Successfully disconnected from the database: " + databaseName);
        } else {
            System.out.println("Database " + databaseName + " is not connected.");
        }
    }
}