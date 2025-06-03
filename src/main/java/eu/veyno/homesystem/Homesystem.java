package eu.veyno.homesystem;

import eu.veyno.homesystem.Converter.HomeConverter;
import eu.veyno.homesystem.Home.DatabaseManager;
import eu.veyno.homesystem.Home.Home;
import eu.veyno.homesystem.Home.HomeCommand;
import eu.veyno.homesystem.Home.UserInterface;
import eu.veyno.homesystem.Warp.WarpCommand;
import eu.veyno.homesystem.Warp.WarpUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Homesystem extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        try {

            saveDefaultResource("Home/config.yml");
            saveDefaultResource("Home/homeicons.yml");
            saveDefaultResource("Home/icons.yml");
            saveDefaultResource("Home/items.yml");
            saveDefaultResource("Home/messages.yml");
            saveDefaultResource("Home/ui.yml");
            saveDefaultResource("Home/ui.yml");
            saveDefaultResource("Database/config.yml");


            DatabaseManager.setPlugin(this);
            setCommandExecutors();
            WarpUI.setPlugin(this);
            HomeCommand.getInstance().setPlugin(this);
            UserInterface.setPlugin(this);
            Bukkit.getPluginManager().registerEvents(this, this);


            DatabaseManager.getInstance().reconnect();

            getLogger().info("Homesystem gestartet!");
        } catch (Exception e){
            e.printStackTrace();
            getLogger().severe("Homesystem konnte nicht gestartet werden!");
        }
    }

    private void saveDefaultResource(String path) {
        File file = new File(getDataFolder(), path);
        if (!file.exists()) {
            getLogger().info("Creating default file: " + path);
            getDataFolder().mkdirs();
            saveResource(path, false);
        }
    }


    private void setCommandExecutors() {
        getCommand("home").setExecutor(HomeCommand.getInstance());
        getCommand("homes").setExecutor(HomeCommand.getInstance());
        getCommand("sethome").setExecutor(HomeCommand.getInstance());
        getCommand("delhome").setExecutor(HomeCommand.getInstance());

        getCommand("warp").setExecutor(WarpCommand.getInstance());
        getCommand("warps").setExecutor(WarpCommand.getInstance());
    }

    @Override
    public void onDisable() {
        getLogger().info("Homesystem Beendet!");
    }

    //current.getBlockX() gibt int zurück
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(Home.getTeleportingPlayers().contains(e.getPlayer().getUniqueId().toString())){
            Home home = Home.getTeleportinghomes().get(e.getPlayer());
            Location start = home.getTpstart();
            Location current = e.getPlayer().getLocation();;
            if((int)start.getX()!=(int)current.getX()){
                home.setNotMoving(false);
            }
            if((int)start.getY()!=(int)current.getY()){
                home.setNotMoving(false);
            }
            if((int)start.getZ()!=(int)current.getZ()){
                home.setNotMoving(false);
            }
        }
    }

}
