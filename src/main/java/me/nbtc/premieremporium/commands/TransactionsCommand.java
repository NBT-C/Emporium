package me.nbtc.premieremporium.commands;

import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TransactionsCommand extends Command {
    public TransactionsCommand() {
        super("transactions");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.msg(sender, "need-a-player");
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("marketplace.transactions")){
            MessageUtil.msg(player, "no-permission");
            return false;
        }

        if (args.length != 0){
            MessageUtil.msg(player, "invalid-argument-transactions");
            return false;
        }

        Emporium.getInstance().getMenuManager().openTransactions(player);
        return false;
    }

}
