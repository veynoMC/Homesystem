package eu.veyno.homesystem.Util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PermissionManager {



    public static int getMaxHomesOfPlayer(Player player){
        for(int i = 100; i >= 0; i--){
            if(player.hasPermission("homesystem.maxhomes." + i)){
                return i;
            }
        }
        return 5;
    }

    public static int getMaxPublicHomesOfPlayer(Player player){
        for(int i = 100; i >= 0; i--){
            if(player.hasPermission("homesystem.maxpublichomes." + i)){
                return i;
            }
        }
        return 1;
    }




}
