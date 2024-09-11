package me.nbtc.premieremporium.manager;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.ItemOwner;
import me.nbtc.premieremporium.base.MarketItem;
import me.nbtc.premieremporium.base.Transaction;
import me.nbtc.premieremporium.base.User;
import me.nbtc.premieremporium.manager.enums.MarketType;
import me.nbtc.premieremporium.utils.MessageUtil;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class MarketManager {
    private List<MarketItem> marketItems = new ArrayList<>();
    private final List<MarketItem> blackMarketItems = new ArrayList<>();

    Random random = new Random();

    public void addItem(Player owner, ItemStack item, double price) {
        owner.getInventory().setItemInMainHand(null);

        UUID ownerId = owner.getUniqueId();
        String ownerName = owner.getName();
        ItemOwner itemOwner = new ItemOwner(ownerName, ownerId);
        MarketItem marketItem = new MarketItem(item, price, itemOwner, UUID.randomUUID());

        marketItems.add(marketItem);

        Map<String, String> replacements = new HashMap<>();
        replacements.put("{item}", item.getType().name().toLowerCase().replace("_", " "));
        replacements.put("{price}", price + "");

        MessageUtil.msg(owner, "sold-successfully", replacements);
    }

    public void handleItemPurchase(Player buyer, MarketItem item){
        User user = Emporium.getInstance().getUserManager().getUser(buyer);

        if (user.getMoney() < item.getPrice()){
            MessageUtil.msg(buyer, "no-enough-money");
            return;
        }

        buyer.getInventory().addItem(item.getItemBehaviour());

        blackMarketItems.removeIf(i -> i.getRandomId().equals(item.getRandomId()));
        marketItems.removeIf(i -> i.getRandomId().equals(item.getRandomId()));

        Map<String, String> replacements = new HashMap<>();
        replacements.put("{item}", item.getItemBehaviour().getType().name().toLowerCase().replace("_", " "));
        replacements.put("{player}", buyer.getName());
        replacements.put("{price}", item.getPrice() + "");

        user.setMoney(user.getMoney() - item.getPrice());
        Emporium.getInstance().getMenuManager().openMarket(buyer, item.isBlack() ? MarketType.BLACK : MarketType.NORMAL);
        MessageUtil.msg(buyer, "bought-successfully", replacements);

        UUID seller = item.getItemOwner().getUuid();
        User userSeller = Emporium.getInstance().getUserManager().getUser(seller);

        double earn = item.isBlack() ? item.getPrice() * 4 : item.getPrice();

        replacements.put("{newprice}", earn + "");

        userSeller.setMoney(userSeller.getMoney() + earn);
        MessageUtil.msg(seller, "bought-item-successfully", replacements);

        Emporium.getInstance().getDiscordManager().sendSoldItemNotification(item.getItemOwner().getName(), item.getItemBehaviour().getType().name().toLowerCase().replace("_", " "), buyer.getName(), item.getPrice() + "");
        Emporium.getInstance().getUserManager().getUser(buyer).getTransactions().add(new Transaction(System.currentTimeMillis(), item));
    }
    public void generateBlackMarket() {
        for (MarketItem item : marketItems) {
            if (random.nextBoolean()) {
                MarketItem blackMarketItem = new MarketItem(item.getItemBehaviour(), item.getPrice(), item.getItemOwner(), item.getRandomId());
                blackMarketItem.setBlack(true);
                blackMarketItem.setPrice(item.getPrice() / 2);
                blackMarketItems.add(blackMarketItem);
            }
        }
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
