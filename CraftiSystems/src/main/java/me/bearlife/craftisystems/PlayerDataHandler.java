package me.bearlife.craftisystems;
import org.bukkit.entity.Player;

import java.io.*;

public class PlayerDataHandler {
    public static void savePlayerData(Player p, String data){

     p.getPlayer();

        File playerFile = new File("plugins/CraftiSystems/playerdata/" + p.getName() + ".txt");
        try {
            playerFile.getParentFile().mkdirs(); // Create directories if they don't exist
            playerFile.createNewFile();

            FileWriter writer = new FileWriter(playerFile);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadPlayerData(Player p){
        p.getPlayer();
        File playerFile = new File("plugins/CraftiSystems/playerdata/" + p.getName() + ".txt");

        try {
            if (playerFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(playerFile));
                String data = reader.readLine();
                reader.close();
                return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
