package eu.veyno.homesystem.Util;


import eu.veyno.homesystem.Home.DatabaseManager;
import eu.veyno.homesystem.Home.Home;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TimeoutManager {

    private static int daysNotPlayed(OfflinePlayer offlinePlayer){
        if (offlinePlayer.hasPlayedBefore()) {
            long lastPlayed = offlinePlayer.getLastPlayed();
            long currentTime = System.currentTimeMillis();

            long differenceInMillis = currentTime - lastPlayed;

            long daysDifference = TimeUnit.MILLISECONDS.toDays(differenceInMillis);

            return (int)daysDifference;
        }
        else{
            return 0;
        }
    }

    public static void checkForWarpTimeout(){
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        List<Home> homes = DatabaseManager.getInstance().getPublicHomes();
        for(OfflinePlayer offlinePlayer : offlinePlayers){
            if(daysNotPlayed(offlinePlayer)>14){
                for(Home home : homes){
                    //Home und databasemanager müssen noch die UUID des home besitzers implementieren
                }
            }
        }
    }
}
