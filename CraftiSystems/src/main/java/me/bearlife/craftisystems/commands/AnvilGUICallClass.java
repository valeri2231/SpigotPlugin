package me.bearlife.craftisystems.commands;

import me.bearlife.craftisystems.CraftiSystems;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.Collections;


public class AnvilGUICallClass implements CommandExecutor{
    ItemStack em = new ItemStack(Material.EMERALD);
    private final CraftiSystems instance;

    public AnvilGUICallClass(CraftiSystems instance){
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String st, String[] strings) {

        Economy economy = CraftiSystems.getEconomy();
        Player p = (Player) s;
        p.getPlayer();
        Inventory inventory = p.getInventory();



        ItemStack ballanceItemName = new ItemStack(Material.BOOK);
        ItemMeta meta = ballanceItemName.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "You have: "+economy.format(economy.getBalance(p)));
        ballanceItemName.setItemMeta(meta);



        new AnvilGUI.Builder()// Creates a new anvil gui interface
                .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    int slots=36;
                    for (ItemStack is : inventory.getContents()) {
                        if (is == null)
                            continue;
                        slots--;
                    }

                   try {
                       double withdrawAmount = Integer.parseInt(stateSnapshot.getText());
                       if(economy.getBalance(p )>= withdrawAmount && withdrawAmount>0) {
                           if(withdrawAmount <= slots*64){
                           EconomyResponse response = economy.withdrawPlayer(p, withdrawAmount);
                           if (response.transactionSuccess()) {
                               p.sendMessage(ChatColor.LIGHT_PURPLE + "You have removed: " + response.amount);
                               p.sendMessage(ChatColor.GOLD +  "Now your balance is: " + response.balance);

                               for (int i = 0; i < withdrawAmount; i++) {
                                   inventory.addItem(em);
                               }
                           }
                       }
                       else {
                           p.sendMessage(ChatColor.RED + "Not enough space in the inventory!");
                            }
                       }
                       else {
                           p.sendMessage(ChatColor.RED+"INVALID INPUT!");
                       }
                   }catch (NumberFormatException e){
                       p.sendMessage(ChatColor.RED+"ONLY NUMBERS ARE ALLOWED!!!");
                   }
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
              //  .preventClose()                                                    //prevents the inventory from being closed
                .text("0")                              //sets the text the GUI should start with
                .itemRight(ballanceItemName)
                .title("...")                                       //set the title of the GUI (only works in 1.14+)
                .plugin(instance)                                                  //set the plugin instance
                .open(p);                                                          //opens the GUI for the player provided



        return true;
    }

}
