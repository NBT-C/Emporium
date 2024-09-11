package me.nbtc.premieremporium.manager;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.Transaction;
import me.nbtc.premieremporium.base.User;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager {
    private final HashMap<UUID, User> users = new HashMap<>();

    public void load(Player player){
        User user = getUser(player);

        MongoDatabase database = Emporium.getInstance().getDataManager().getDatabase();
        MongoCollection<Document> collection = database.getCollection("emporium");

        Document userDoc = collection.find(new Document("uuid", user.getUuid().toString())).first();

        if (userDoc != null) {
            user.setMoney(userDoc.getDouble("money"));
            user.setTransactions(Emporium.getInstance().getGson().fromJson(userDoc.getString("transactions"), new TypeToken<List<Transaction>>() {}.getType()));
        }
    }

    public void save(Player player){
        User user = getUser(player);
        MongoDatabase database = Emporium.getInstance().getDataManager().getDatabase();
        MongoCollection<Document> collection = database.getCollection("emporium");

        Document userDoc = new Document("uuid", user.getUuid().toString())
                .append("ign", user.getName())
                .append("money", user.getMoney())
                .append("transactions", Emporium.getInstance().getGson().toJson(user.getTransactions()));

        collection.replaceOne(new Document("uuid", user.getUuid().toString()), userDoc, new ReplaceOptions().upsert(true));
    }
    public User getUser(@NotNull Player p) {
        return this.users.computeIfAbsent(p.getUniqueId(), k -> (new User(p.getUniqueId(), p.getName())));
    }
}
