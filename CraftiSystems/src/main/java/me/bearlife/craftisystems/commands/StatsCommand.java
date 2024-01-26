
package me.bearlife.craftisystems.commands;
import me.bearlife.craftisystems.CraftiSystems;
import me.bearlife.craftisystems.managers.PlayerBountyManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StatsCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        Player p = (Player) sender;
        Economy economy = CraftiSystems.getEconomy();


        Inventory inventory = p.getInventory();

        PlayerBountyManager Bounty = new PlayerBountyManager();
        Bounty.addBounty(p, 100);

        p.sendMessage("Your bounty is: " + Bounty.getBounty(p));

        ItemStack[] playerItems = inventory.getContents();
        int moneyCount = 0; // Initialize the count to zero

        for (ItemStack item : playerItems) {
            if (item != null && item.getType() == Material.EMERALD) {
                // Increment the count for each diamond found
                moneyCount += item.getAmount();
            }
        }



        if (moneyCount > 0) {
           // p.sendMessage(ChatColor.GOLD+"You have " + moneyCount + " emerald(s) in your inventory.");
        } else {
            //p.sendMessage(ChatColor.RED+"You don't have any emeralds in your inventory.");
        }


        if (strings.length==0) {
            p.sendMessage(  ChatColor.GOLD + "Balance: " + economy.format(economy.getBalance(p)));
        }
        else if (strings.length==2 && strings[0].equalsIgnoreCase("withdraw")) {
            int withdraw_amount = Integer.valueOf(strings[1]);
            EconomyResponse response = economy.withdrawPlayer(p, withdraw_amount);
            if (response.transactionSuccess()){
                p.sendMessage( ChatColor.LIGHT_PURPLE+"You have removed: " + response.amount);
                p.sendMessage( ChatColor.GREEN+"Now your balance is: " + response.balance);
            }}
            else if (strings.length==1 && strings[0].equalsIgnoreCase("deposit")){
                int deposit_amount = Integer.valueOf(moneyCount);
            EconomyResponse response = economy.depositPlayer(p, deposit_amount);
            inventory.remove(Material.EMERALD);
                if (response.transactionSuccess()){
                    p.sendMessage(ChatColor.LIGHT_PURPLE+"You have added: " + response.amount);
                    p.sendMessage(ChatColor.GOLD+"Now your balance is: " + response.balance);
                }
            }


        return true;
    }
}
