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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.Collections;

public class TransferGUICallClass implements CommandExecutor {


    private final CraftiSystems instance;
    public TransferGUICallClass(CraftiSystems instance){
        this.instance = instance;
    }



    private void TransferGUIMethod(Player p, ItemStack ballanceItemName, String input, Economy economy){
        new AnvilGUI.Builder()// Creates a new anvil gui interface
                .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    ItemMeta bmeta = ballanceItemName.getItemMeta();
                    bmeta.setDisplayName(ChatColor.DARK_GREEN + "You have: "+economy.format(economy.getBalance(p)));
                    ballanceItemName.setItemMeta(bmeta);
                    try {

                        double withdrawAmount = Integer.parseInt(stateSnapshot.getText());

                     //   String input = stateSnapshot.getText();
                        Player secondPlayer = Bukkit.getPlayer(input);

                        if(economy.getBalance(p)>=withdrawAmount && withdrawAmount>0) {
                            EconomyResponse response1 = economy.withdrawPlayer(p, withdrawAmount);
                            EconomyResponse response2 = economy.depositPlayer(secondPlayer, withdrawAmount);

                            if (response1.transactionSuccess()) {
                                p.sendMessage(ChatColor.LIGHT_PURPLE + "You gave " + response1.amount +" "+ "to "+ secondPlayer.getName() );
                                p.sendMessage(ChatColor.GOLD +" " + "Now your balance is: " + response1.balance);
                                secondPlayer.sendMessage(ChatColor.LIGHT_PURPLE + "You received " + withdrawAmount+" "+ "from "+ p.getName());
                                secondPlayer.sendMessage(ChatColor.GOLD +  "Now your balance is: " +" "+ response2.balance);
                            }
                        }
                        else {
                            p.sendMessage(ChatColor.RED +"INVALID INPUT!");
                            return Arrays.asList(AnvilGUI.ResponseAction.close());
                        }
                    }catch (NumberFormatException e){
                        p.sendMessage(ChatColor.RED+"ONLY NUMBERS ARE ALLOWED!!!");
                        return Arrays.asList(AnvilGUI.ResponseAction.close());
                    }
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
                //  .preventClose()                                                    //prevents the inventory from being closed
                .text("0")                              //sets the text the GUI should start with
                .itemRight(ballanceItemName)
                .title("...")                                       //set the title of the GUI (only works in 1.14+)
                .plugin(CraftiSystems.getInstance())                                                  //set the plugin instance
                .open(p);                                                          //opens the GUI for the player provided

    }
    @Override
    public boolean onCommand(CommandSender s, Command command, String st, String[] strings) {


        Economy economy = CraftiSystems.getEconomy();
        Player p = (Player) s;
        p.getPlayer();
        Inventory inventory = p.getInventory();

        int slots=0;
        for (ItemStack itenStack : inventory){
            if(itenStack == null){
                slots++;
            }
        }

        ItemStack em = new ItemStack(Material.EMERALD);
        ItemStack namesItemName = new ItemStack(Material.BOOK);
        ItemMeta meta = namesItemName.getItemMeta();
        meta.setDisplayName("Random");
        namesItemName.setItemMeta(meta);

        ItemStack ballanceItemName = new ItemStack(Material.BOOK);
        ItemMeta bmeta = ballanceItemName.getItemMeta();
        bmeta.setDisplayName(ChatColor.DARK_GREEN + "You have: "+economy.format(economy.getBalance(p)));
        ballanceItemName.setItemMeta(bmeta);



        new AnvilGUI.Builder()// Creates a new anvil gui interface

                .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String input = stateSnapshot.getText();
                    Player secondPlayer = Bukkit.getPlayer(input);

                    if(secondPlayer != null && secondPlayer.isOnline()){
                        if(secondPlayer.getName() != p.getName()) {
                            TransferGUIMethod(p.getPlayer(), ballanceItemName, stateSnapshot.getText(), economy);
                        }
                        else {
                            p.sendMessage(ChatColor.RED + "Cannot transfer to yourself!");
                        }
                    }
                    else {
                p.sendMessage(ChatColor.RED+"This player either doesn't exist or in not on the server at the moment!");
                    }
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })

                //  .preventClose()                                                    //prevents the inventory from being closed
                .text("Player name")                              //sets the text the GUI should start with
                .itemRight(namesItemName)
                .title("...")                                       //set the title of the GUI (only works in 1.14+)
                .plugin(instance)                                                  //set the plugin instance
                .open(p);                                                          //opens the GUI for the player provided


        return true;
    }

}
