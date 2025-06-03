package eu.veyno.homesystem.Home;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class Home {
    private Location location;
    private String title;
    private String description = " ";
    private String HomeID = UUID.randomUUID().toString();
    private int icon = 1;
    private boolean isPublic = false;
    private boolean notMoving = true;

    private File file;
    private FileConfiguration configfile;
    private File file1;
    private FileConfiguration messages;

    private static List<String> teleportingPlayers = new ArrayList<>();
    private static HashMap<Player, Home> teleportinghomes = new HashMap<>();
    private Location tpstart;

    public Home(Location location, String title){
        this.location = location;
        this.title = title;
        File pluginFolder = Bukkit.getPluginManager().getPlugin("Homesystem").getDataFolder();
        File homefolder = new File(pluginFolder, "Home");
        if (!homefolder.exists()) {
            homefolder.mkdirs();
        }
        file = new File(homefolder, "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file1 = new File(homefolder, "messages.yml");
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        messages = YamlConfiguration.loadConfiguration(file1);
        configfile = YamlConfiguration.loadConfiguration(file);
    }

    public void teleport(Player player){
        if(teleportingPlayers.contains(player.getUniqueId().toString())){
            player.sendMessage(messages.getString("prefix") + messages.getString("alreadyeleproting"));
        }else {
            if(!(location.getBlock().isPassable())){
                player.sendMessage(messages.getString("prefix") + messages.getString("unsafetp"));
                return;
            }
            tpstart = player.getLocation();
            notMoving = true;
            teleportinghomes.put(player, this);
            teleportingPlayers.add(player.getUniqueId().toString());
            new BukkitRunnable() {
                int countdown = configfile.getInt("teleportdelay");
                @Override
                public void run() {
                    if(!notMoving){
                        player.sendMessage(messages.getString("prefix") + messages.getString("teleportcancel"));
                        notMoving = true;
                        teleportingPlayers.remove(player.getUniqueId().toString());
                        this.cancel();
                    }else if (countdown > 0) {
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
                        player.sendMessage(messages.getString("prefix") + messages.getString("teleportcountdown") + " " + countdown);
                        countdown--;
                    } else{
                        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.sendMessage(messages.getString("prefix") + messages.getString("teleportsuccess"));
                        player.teleport(Home.this.location);
                        teleportingPlayers.remove(player.getUniqueId().toString());
                        this.cancel();
                    }
                }
            }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Homesystem"), 0, 20);
        }
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public World getWorld(){
        return location.getWorld();
    }

    public Location getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setHomeID(String homeID) {
        HomeID = homeID;
    }

    public String getHomeID() {
        return HomeID;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public static List<String> getTeleportingPlayers() {
        return teleportingPlayers;
    }

    public static HashMap<Player, Home> getTeleportinghomes() {
        return teleportinghomes;
    }

    public void setNotMoving(boolean notMoving) {
        this.notMoving = notMoving;
    }

    public Location getTpstart() {
        return tpstart;
    }
}
