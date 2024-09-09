package me.nbtc.premieremporium.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.ItemOwner;
import me.nbtc.premieremporium.base.MarketItem;
import me.nbtc.premieremporium.base.Transaction;
import me.nbtc.premieremporium.utils.MessageUtil;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class MarketRepository {
    private List<MarketItem> marketItems = new ArrayList<>();

    public void addItem(Player owner, ItemStack item, double price) {
        owner.getInventory().setItemInMainHand(null);

        UUID ownerId = owner.getUniqueId();
        String ownerName = owner.getName();
        ItemOwner itemOwner = new ItemOwner(ownerName, ownerId);
        MarketItem marketItem = new MarketItem(item, price, itemOwner);

        marketItems.add(marketItem);
    }

    public void handleItemPurchase(Player buyer, MarketItem item){
        buyer.getInventory().addItem(item.getItem());
        marketItems.remove(item);

        Emporium.getInstance().getRepository(MenuRepository.class).openMarketPlace(buyer.getPlayer());
        MessageUtil.msg(buyer, "bought-successfully");

        Emporium.getInstance().getUser(buyer).getTransactions().add(new Transaction(System.currentTimeMillis(), item));
    }
    public void saveToMongo(){
        String serialized = Emporium.getInstance().getGson().toJson(marketItems);
        try {
            MongoDatabase database = Emporium.getInstance().getDataManager().getDatabase();
            MongoCollection<Document> collection = database.getCollection("emporium");

            Document doc = new Document("market", "normal")
                    .append("marketItems", serialized);

            collection.replaceOne(new Document("market", "normal"), doc, new ReplaceOptions().upsert(true));
        } catch (Exception e) {
            System.out.println("MongoDB error while marketing " + e.getMessage());
        }
    }
    public void loadFromMongo(){
        try {
            MongoDatabase database = Emporium.getInstance().getDataManager().getDatabase();
            MongoCollection<Document> collection = database.getCollection("emporium");

            Document doc = collection.find(new Document("market", "normal")).first();

            if (doc != null) {
                this.marketItems = Emporium.getInstance().getGson().fromJson(doc.getString("marketItems"), new TypeToken<List<MarketItem>>() {}.getType());
            }
        } catch (Exception e) {
            System.out.println("MongoDB error while marketing " + e.getMessage());
        }
    }
}
