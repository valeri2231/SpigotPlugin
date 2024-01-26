package me.bearlife.craftisystems.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerBountyManager {
    public static HashMap<UUID, Integer> PlayerBounty = new HashMap<>();

    public void addBounty(Player p, int amount){

        if(PlayerBounty.get(p.getUniqueId()) != null){

            int currentAmount = PlayerBounty.get(p.getUniqueId());
            int newAmount = currentAmount + amount;
            PlayerBounty.put(p.getUniqueId(), newAmount);
        } else PlayerBounty.put(p.getUniqueId(), 0);

    }

    public void removeBounty(Player p, int amount){

        if(PlayerBounty.get(p.getUniqueId()) != null){

            int currentAmount = PlayerBounty.get(p.getUniqueId());
            int newAmount = currentAmount - amount;
            PlayerBounty.put(p.getUniqueId(), newAmount);
        } else PlayerBounty.put(p.getUniqueId(), 0);

    }

    public void setBounty(Player p,int amount){
        PlayerBounty.put(p.getUniqueId(), amount);
    }

    public Integer getBounty(Player p){

        PlayerBounty.putIfAbsent(p.getUniqueId(), null);
        return PlayerBounty.get(p.getUniqueId());
    }

}
