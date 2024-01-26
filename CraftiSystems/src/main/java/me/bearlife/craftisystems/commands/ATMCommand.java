package me.bearlife.craftisystems.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ATMCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String st, String[] strings) {
        Player p = (Player) s;
        s.getName();
//--------------------------------------------------------------------------------------------------------------------

        ItemStack[] playerItems = p.getInventory().getContents();
        Material itemType = Material.EMERALD;
        int amount=0;
        int itemsRemoved = 0;
        for (ItemStack item : playerItems) {
            if (item != null && item.getType() == itemType) {
                int itemAmount = item.getAmount();
                if (itemAmount <= amount - itemsRemoved) {
                    // Remove the entire stack
                    p.getInventory().removeItem(item);
                    itemsRemoved += itemAmount;
                } else {
                    // Partial removal
                    item.setAmount(itemAmount - (amount - itemsRemoved));
                    itemsRemoved = amount;
                    break; // No need to continue looping
                }
            }
        }
        if (itemsRemoved == amount) {
          //  p.sendMessage("Removed " + amount + " " + itemType.toString() + " from your inventory.");
        } else {
           // p.sendMessage("You don't have enough " + itemType.toString() + " in your inventory.");
        }
        //--------------------------------------------------------------------------------------------------------------------

        Inventory inv = Bukkit.createInventory(p, 9, ChatColor.DARK_GREEN+"ATM"); //9 18 27 36 45 54 size of the inventory

        ItemStack withdraw = new ItemStack(Material.EMERALD, 1);// Adds an item to the newly created inventory
        ItemStack deposit = new ItemStack(Material.EMERALD, 1);
        ItemStack transfer = new ItemStack(Material.WRITABLE_BOOK, 1);

        ItemMeta withdrawMeta = withdraw.getItemMeta(); // Creates a meta for the item basically like nbt data
        ItemMeta depositMeta = deposit.getItemMeta();
        ItemMeta transferMeta = transfer.getItemMeta();

        withdrawMeta.setDisplayName(ChatColor.RED+"Withdraw");// Creates a description for the item you are about to click
        depositMeta.setDisplayName(ChatColor.GREEN+"Deposit");
        transferMeta.setDisplayName(ChatColor.GOLD+"Transfer");

        ArrayList<String> withdrawlore = new ArrayList<>(); // <- we create an array of strings to add lore to an item
        withdrawlore.add("By clicking this you will withdraw");
        withdrawlore.add("from your account");

        ArrayList<String> depositlore = new ArrayList<>(); // <- we create an array of strings to add lore to an item
        depositlore.add("By clicking this you will deposit");
        depositlore.add("to your account");

        ArrayList<String> transferlore = new ArrayList<>(); // <- we create an array of strings to add lore to an item
        transferlore.add("By clicking this you will transfer");
        transferlore.add("money to another player");

        withdrawMeta.setLore(withdrawlore);
        depositMeta.setLore(depositlore);
        transferMeta.setLore(transferlore);

        withdraw.setItemMeta(withdrawMeta);
        deposit.setItemMeta(depositMeta);
        transfer.setItemMeta(transferMeta);

        //inv.addItem(); <- will add the item to the first available slot in the inv
        inv.setItem(2, deposit);
        inv.setItem(4, withdraw);
        inv.setItem(6, transfer);


        p.openInventory(inv);// <- opens the newly created inventory with all the stuff in it as a interface
        return true;
    }
}
