package me.bearlife.craftisystems;

import me.bearlife.craftisystems.commands.*;
import me.bearlife.craftisystems.managers.PlayerBountyManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CraftiSystems extends JavaPlugin implements Listener {
    private static CraftiSystems plugin;
    public static CraftiSystems getInstance() {
        return plugin;
    }
    private static Economy econ = null;




    @Override
    public void onEnable() {   // Plugin startup logic
        plugin = this;


        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new ATMListener(), this);
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("withdrawgui").setExecutor(new AnvilGUICallClass(this));
        getCommand("transfergui").setExecutor(new TransferGUICallClass(this));
        getCommand("ATM").setExecutor(new ATMCommand());
        getCommand("bounty").setExecutor(new BountyCommand());
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);

        }

        }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private final Map<UUID, Long> playerLookTimes = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        PlayerBountyManager Bounty = new PlayerBountyManager();
        new BukkitRunnable() {
            @Override
            public void run() {
                RayTraceResult result = p.getWorld().rayTrace(p.getEyeLocation(), p.getEyeLocation().getDirection(), 7D,
                        FluidCollisionMode.ALWAYS, true, 1, entity -> entity instanceof Player && !entity.equals(p));

                if (result != null && result.getHitEntity() instanceof Player) {
                    Player targetPlayer = (Player) result.getHitEntity();

                    double distance = p.getLocation().distance(targetPlayer.getLocation());

                    if (distance <= 7) {
                        // Check if the playerLookTimes map contains the target player
                        if (!playerLookTimes.containsKey(targetPlayer.getUniqueId())) {
                            // If not, add the target player with the current time
                            playerLookTimes.put(targetPlayer.getUniqueId(), System.currentTimeMillis());
                        } else {
                            // If yes, check if the player has been looking for at least 2 seconds
                            long startTime = playerLookTimes.get(targetPlayer.getUniqueId());
                            if (System.currentTimeMillis() - startTime >= 1000) {
                                // Display information on the action bar
                                sendActionBar(p, ChatColor.GRAY + targetPlayer.getName() + ChatColor.DARK_GRAY + " , " + ChatColor.BLUE +"Civilian" + ChatColor.GRAY +" , "+ Bounty.getBounty(p));
                            }
                        }
                    } else {
                        // Clear the start time if the player is not looking at another player
                        playerLookTimes.remove(targetPlayer.getUniqueId());
                    }
                }
            }
        }.runTask(CraftiSystems.this); // Assuming you are inside the main class of your plugin
    }



    // Method to send action bar message to a player
    private void sendActionBar(Player p, String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory) {
            AnvilInventory anvil = (AnvilInventory) event.getInventory();
            Player player = (Player) event.getWhoClicked();

            if (event.getRawSlot() == 2) {
                String inputText = anvil.getRenameText();
                //player.sendMessage("You input: " + inputText);
                event.setCancelled(true);
            }
        }
    }


   /* @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();

            // Check if the damage was fatal
            if (victim.getHealth() - e.getFinalDamage() <= 0) {
                PlayerDataHandler data = new PlayerDataHandler();
                PlayerDataHandler.loadPlayerData(attacker);
            }
        }
    }
*/
    @Override
    public void onDisable() { // Plugin shutdown logic
        System.out.println("Plugin has stopped");
    }

    public static CraftiSystems getPlugin() {
        return plugin;
    }

    public static Economy getEconomy() {
        return econ;
    }
}





