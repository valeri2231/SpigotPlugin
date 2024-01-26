package me.bearlife.craftisystems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class ATMListener implements Listener {

    @EventHandler
    public void onATMClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "ATM")) {

            if (e.getCurrentItem() == null) { //<- Checks to see if player clicked on an empty slot
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Withdraw")) {//<- Checks if player clicked on the emerald with this name in particular
                String withdrawcommand = "withdrawgui";
                Bukkit.dispatchCommand((CommandSender)p, withdrawcommand);
                }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Deposit")) {//<- Checks if player clicked on the emerald with this name in particular
                String depositcommand = "stats deposit";
                Bukkit.dispatchCommand((CommandSender)p, depositcommand);
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Transfer")) {//<- Checks if player clicked on the emerald with this name in particular
                String transfercommand = "transfergui";
                Bukkit.dispatchCommand((CommandSender)p, transfercommand);
            }
            e.setCancelled(true);// make it so that players cannot drag items around
            }

        }
    }