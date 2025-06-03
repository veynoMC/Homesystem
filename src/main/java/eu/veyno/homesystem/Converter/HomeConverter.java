package eu.veyno.homesystem.Converter;

import eu.veyno.homesystem.Home.DatabaseManager;
import eu.veyno.homesystem.Home.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Set;

public class HomeConverter {

    public static void convertHomes() {
        File pluginFolder = Bukkit.getPluginManager().getPlugin("homesystem").getDataFolder();
        File homeFolder = new File(pluginFolder, "Home/playerdata");

        if (!homeFolder.exists() || !homeFolder.isDirectory()) {
            System.out.println("Kein gültiger Home-Ordner gefunden.");
            return;
        }

        File[] playerFiles = homeFolder.listFiles();
        if (playerFiles == null || playerFiles.length == 0) {
            System.out.println("Keine Player-Dateien gefunden.");
            return;
        }

        for (File playerFile : playerFiles) {
            if (!playerFile.getName().endsWith(".yml")) continue;
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            String uuid = playerFile.getName().replace(".yml", "");
            if (!playerConfig.contains("homes")) continue;
            Set<String> homeNames = playerConfig.getConfigurationSection("homes").getKeys(false);
            for (String homeName : homeNames) {
                String path = "homes." + homeName;
                double x = playerConfig.getDouble(path + ".x");
                double y = playerConfig.getDouble(path + ".y");
                double z = playerConfig.getDouble(path + ".z");
                float yaw = (float) playerConfig.getDouble(path + ".yaw");
                float pitch = (float) playerConfig.getDouble(path + ".pitch");
                String worldName = playerConfig.getString(path + ".world");
                Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                Home home = new Home(location, homeName);
                home.setPublic(false);
                DatabaseManager.getInstance().saveHome(null /*uuid*/, home);
            }
        }

        System.out.println("Alle Homes wurden erfolgreich konvertiert!");
    }
}

