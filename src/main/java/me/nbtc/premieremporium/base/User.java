package me.nbtc.premieremporium.base;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Data;
import me.nbtc.premieremporium.Emporium;
import org.bson.Document;

import java.util.*;

@Data
public class User {
    private final UUID uuid;
    private final String name;
    private double money = 100.0;
    private List<Transaction> transactions;

    public User(UUID uuid, String name){
        this.uuid = uuid;
        this.name = name;
        this.transactions = new ArrayList<>();
    }
    public User load() {
        try {
            MongoDatabase database = Emporium.getInstance().getDataManager().getDatabase();
            MongoCollection<Document> collection = database.getCollection("emporium");

            Document userDoc = collection.find(new Document("uuid", this.uuid.toString())).first();

            if (userDoc != null) {
                this.money = userDoc.getDouble("money");
                this.transactions = Emporium.getInstance().getGson().fromJson(userDoc.getString("transactions"), new TypeToken<List<Transaction>>() {}.getType());
            }
            return this;
        } catch (Exception e) {
            System.out.println("MongoDB error while loading [" + this.uuid + "]: " + e.getMessage());
            return null;
        }
    }
    public void save() {
        try {
            MongoDatabase database = Emporium.getInstance().getDataManager().getDatabase();
            MongoCollection<Document> collection = database.getCollection("emporium");

            Document userDoc = new Document("uuid", this.uuid.toString())
                    .append("ign", this.name)
                    .append("money", this.money)
                    .append("transactions", Emporium.getInstance().getGson().toJson(transactions));

            collection.replaceOne(new Document("uuid", this.uuid.toString()), userDoc, new ReplaceOptions().upsert(true));
        } catch (Exception e) {
            System.out.println("MongoDB error while saving [" + this.uuid + "]: " + e.getMessage());
        }
    }

}
