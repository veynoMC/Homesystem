package eu.veyno.homesystem.Home;

import eu.veyno.homesystem.Util.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static Plugin plugin;
    private File file;
    private FileConfiguration config;
    private String url = "";
    private String username = "";
    private String password = "";
    private boolean localdbInUse = false;

    private static Connection connection;

    private static DatabaseManager instance = new DatabaseManager();

    private DatabaseManager() {
    }

    public static void setPlugin(Plugin plugin) {
        DatabaseManager.plugin = plugin;
    }

    public List<Home> getPublicHomes() {
        if (!isConnected()) reconnect();
        List<Home> publicHomes = new ArrayList<>();
        String sql = "SELECT HomeID, UUID, Name, Description, Icon, x, y, z, public, World FROM home WHERE public = TRUE";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String homeID = rs.getString("HomeID");
                String name = rs.getString("Name");
                String description = rs.getString("Description");
                int icon = rs.getInt("Icon");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                String world = rs.getString("World");

                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                Home home = new Home(location, name);
                home.setHomeID(homeID);
                home.setIcon(icon);
                home.setDescription(description);
                home.setPublic(true);
                publicHomes.add(home);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicHomes;
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public void reconnect() {
        connect();
    }

    public boolean isConnected() {
        try {
            return connection != null && connection.isValid(60);
        } catch (SQLException e) {
            return false;
        }
    }


//    public boolean saveHome(String UUID, Home newHome){
//        if (!isConnected()) reconnect();
//        String sql = "INSERT INTO home (HomeID, UUID, Name, Description, Icon, x, y, z, public, World) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
//                "ON DUPLICATE KEY UPDATE Description = ?, Icon = ?, x = ?, y = ?, z = ?, public = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//
//            stmt.setString(1, newHome.getHomeID());
//            stmt.setString(2, UUID);
//            stmt.setString(3, newHome.getTitle());
//            stmt.setString(4, newHome.getDescription());
//            stmt.setInt(5, newHome.getIcon());
//            stmt.setInt(6, newHome.getLocation().getBlockX());
//            stmt.setInt(7, newHome.getLocation().getBlockY());
//            stmt.setInt(8, newHome.getLocation().getBlockZ());
//            stmt.setBoolean(9, newHome.isPublic());
//            stmt.setString(10, newHome.getWorld().getName());
//
//            // Update-Parameter
//            stmt.setString(11, newHome.getDescription());
//            stmt.setInt(12, newHome.getIcon());
//            stmt.setInt(13, newHome.getLocation().getBlockX());
//            stmt.setInt(14, newHome.getLocation().getBlockY());
//            stmt.setInt(15, newHome.getLocation().getBlockZ());
//            stmt.setBoolean(16, newHome.isPublic());
//
//            stmt.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean saveHome(Player player, Home newHome) {
        if (!isConnected()) reconnect();
        if (getAllHomesOfPlayer(player.getUniqueId().toString()).size() >= PermissionManager.getMaxHomesOfPlayer(player)) {
            return false;
        }
        String UUID = player.getUniqueId().toString();
        String sql;
        if(localdbInUse){
            sql = "INSERT INTO home (HomeID, UUID, Name, Description, Icon, x, y, z, public, World) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT(HomeID) DO UPDATE SET " +
                    "Description = excluded.Description, " +
                    "Icon = excluded.Icon, " +
                    "x = excluded.x, " +
                    "y = excluded.y, " +
                    "z = excluded.z, " +
                    "public = excluded.public";
        } else {
            sql = "INSERT INTO home (HomeID, UUID, Name, Description, Icon, x, y, z, public, World) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE Description = ?, Icon = ?, x = ?, y = ?, z = ?, public = ?";
        }
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, newHome.getHomeID());
            stmt.setString(2, UUID);
            stmt.setString(3, newHome.getTitle());
            stmt.setString(4, newHome.getDescription());
            stmt.setInt(5, newHome.getIcon());
            stmt.setInt(6, newHome.getLocation().getBlockX());
            stmt.setInt(7, newHome.getLocation().getBlockY());
            stmt.setInt(8, newHome.getLocation().getBlockZ());
            stmt.setBoolean(9, newHome.isPublic());
            stmt.setString(10, newHome.getWorld().getName());

            if(!localdbInUse) {
                stmt.setString(11, newHome.getDescription());
                stmt.setInt(12, newHome.getIcon());
                stmt.setInt(13, newHome.getLocation().getBlockX());
                stmt.setInt(14, newHome.getLocation().getBlockY());
                stmt.setInt(15, newHome.getLocation().getBlockZ());
                stmt.setBoolean(16, newHome.isPublic());
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delHome(String HomeID) {
        if (!isConnected()) reconnect();
        String sql = "DELETE FROM home WHERE HomeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, HomeID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Home> getAllHomesOfPlayer(String UUID) {
        if (!isConnected()) reconnect();
        List<Home> homes = new ArrayList<>();
        String sql = "SELECT HomeID, UUID, Name, Description, Icon, x, y, z, public, World FROM home WHERE UUID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, UUID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String homeID = rs.getString("HomeID");
                String name = rs.getString("Name");
                String description = rs.getString("Description");
                int icon = rs.getInt("Icon");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");
                boolean isPublic = rs.getBoolean("public");
                String world = rs.getString("World");
                Location location = new Location(Bukkit.getWorld(world), x, y, z);
                Home home = new Home(location, name);
                home.setHomeID(homeID);
                home.setIcon(icon);
                home.setDescription(description);
                home.setPublic(isPublic);
                homes.add(home);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homes;
    }

    public int getPlayerPublicHomeCount(Player player){
        int count = 0;
        for(Home home : getAllHomesOfPlayer(player.getUniqueId().toString())){
            if(home.isPublic()) count++;
        }
        return count;
    }

    private void createTableIfNotExists() {
        if(!isConnected()) reconnect();
        String sql;
        if (!localdbInUse) {
            sql = "CREATE TABLE IF NOT EXISTS home ( " +
                    "HomeID TEXT PRIMARY KEY, UUID TEXT, Name TEXT, Description TEXT, Icon INTEGER, " +
                    "x INTEGER, y INTEGER, z INTEGER, public BOOLEAN, World TEXT )";
        } else {
            sql = "CREATE TABLE IF NOT EXISTS home ( " +
                    "HomeID VARCHAR(36) PRIMARY KEY, UUID VARCHAR(36), Name VARCHAR(255), Description TEXT, Icon INT, " +
                    "x INT, y INT, z INT, public BOOLEAN, World VARCHAR(255) )";
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setConnection(Connection connection) {
        DatabaseManager.connection = connection;
    }

    private void connect() {
        Bukkit.getLogger().info("connecting database...");
        File pluginFolder = Bukkit.getPluginManager().getPlugin("Homesystem").getDataFolder();
        File configfolder = new File(pluginFolder, "Database");
        if (!configfolder.exists()) {
            configfolder.mkdirs();
        }

        file = new File(configfolder, "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("connection")) {
            config.set("connection", "local");
            try {
                config.save(file);
            } catch (IOException e) {
                Bukkit.getLogger().warning("Error while saving Database configuration: " + e.getMessage());
            }
        }

        url = config.getString("connection");

        // Fixed logic: connect to local database when config says "local"
        if (url == null || url.equalsIgnoreCase("local")) {
            Bukkit.getLogger().info("connecting to local database");
            localdbInUse = true;  // Set this flag for table creation
            connectLocal();
            return;
        }

        Bukkit.getLogger().info("connecting to remote database");
        localdbInUse = false;

    Bukkit.getScheduler().runTaskAsynchronously(plugin, p -> {
        try {
            DatabaseManager.setConnection(DriverManager.getConnection(url, username, password));
            createTableIfNotExists();
            Bukkit.getLogger().info("MySQL database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("MySQL connection error: " + e.getMessage());
        }
    });

    }


    private void connectLocal() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, p -> {
            File pluginFolder = plugin.getDataFolder();

            if (!pluginFolder.exists()) {
                pluginFolder.mkdirs();
            }

            File configfolder = new File(pluginFolder, "Database");
            if (!configfolder.exists()) {
                configfolder.mkdirs();
            }

            File dbFile = new File(configfolder, "homes.db");
            String dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            Bukkit.getLogger().info("connecting to: " + dbUrl);

            try {
                DatabaseManager.setConnection(DriverManager.getConnection(dbUrl));
                createTableIfNotExists();
                Bukkit.getLogger().info("local database connected successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getLogger().severe("local database connection error: " + e.getMessage());
            }
        });
    }
}