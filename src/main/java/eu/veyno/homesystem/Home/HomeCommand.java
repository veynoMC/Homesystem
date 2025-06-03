package eu.veyno.homesystem.Home;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class HomeCommand implements CommandExecutor {
    private static HomeCommand instance;
    private Plugin plugin;
    private File file;
    private FileConfiguration ui;


    private HomeCommand(){
        File pluginFolder = Bukkit.getPluginManager().getPlugin("Homesystem").getDataFolder();
        File discordConfigsFolder = new File(pluginFolder, "Home");
        if (!discordConfigsFolder.exists()) {
            discordConfigsFolder.mkdirs();
        }
        file = new File(discordConfigsFolder, "ui.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ui = YamlConfiguration.loadConfiguration(file);
    }

    public static HomeCommand getInstance(){
        if(instance == null){
            instance = new HomeCommand();
        }
        return instance;
    }


    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ui.getString("prefix") + "Dieser Befehl kann nur von einem Spieler verwendet werden.");
            return true;
        }
        if(command.getName().equalsIgnoreCase("home")
        ||command.getName().equalsIgnoreCase("homes")
        ||command.getName().equalsIgnoreCase("delhome")){
            UserInterface ui = UserInterface.getInstance(player);
            ui.openMainPage();
        }
        else if(command.getName().equalsIgnoreCase("sethome")){
            if (args.length>0) {
                Home home = new Home(player.getLocation(), args[0]);
                UserInterface.getInstance(player).saveHome(home, player);
                UserInterface.getInstance(player).refresh();
            }
        }
        return true;
    }

}
