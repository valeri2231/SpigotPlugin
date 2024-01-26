package me.bearlife.craftisystems.commands;

import me.bearlife.craftisystems.managers.PlayerBountyManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command command, String S, String[] args) {
        Player p = (Player) s;
        s.getName();

        PlayerBountyManager Bounty = new PlayerBountyManager();
        int value = Integer.valueOf(args[1]);

        if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            Bounty.addBounty(p,value);
            p.sendMessage("You added " + value + "to your bounty");
            p.sendMessage("Your bounty is " + Bounty.getBounty(p));
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
            Bounty.removeBounty(p,value);
            p.sendMessage("You removed " + value + "from your bounty");
            p.sendMessage("Your bounty is " + Bounty.getBounty(p));
        }
        else {p.sendMessage("Something went wrong!!");}

        return true;
    }
}