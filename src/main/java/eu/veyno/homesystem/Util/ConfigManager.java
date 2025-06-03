package eu.veyno.homesystem.Util;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private File file;
    private FileConfiguration items;

    private static ConfigManager instance = new ConfigManager();

    private ConfigManager(){
        init();
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    private void init(){
        File pluginFolder = Bukkit.getPluginManager().getPlugin("Homesystem").getDataFolder();
        File discordConfigsFolder = new File(pluginFolder, "Home");
        if (!discordConfigsFolder.exists()) {
            discordConfigsFolder.mkdirs();
        }
        file = new File(discordConfigsFolder, "items.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        items = YamlConfiguration.loadConfiguration(file);
    }

    public ItemStack getIcon(String path, FileConfiguration config){
        init();
        if(path.equalsIgnoreCase("nextPageArrow")){
            ItemStack itemStack = new ItemStack(Material.ARROW);
            //ItemStack itemStack = new ItemStack(Material.STRING);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(6);
            itemMeta.setDisplayName(items.getString("nextpagearrow.title"));
            List<String> lore = items.getStringList("nextpagearrow.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("previousPageArrow")){
            ItemStack itemStack = new ItemStack(Material.ARROW);
            //ItemStack itemStack = new ItemStack(Material.BONE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(5);
            itemMeta.setDisplayName(items.getString("previouspagearrow.title"));
            List<String> lore = items.getStringList("previouspagearrow.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("deleteconfirm")){
            ItemStack itemStack = new ItemStack(Material.GREEN_WOOL);
            //ItemStack itemStack = new ItemStack(Material.COAL);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(2);
            itemMeta.setDisplayName(items.getString("deleteconfirm.title"));
            List<String> lore = items.getStringList("deleteconfirm.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("deletecancel")){
            ItemStack itemStack = new ItemStack(Material.RED_WOOL);
            //ItemStack itemStack = new ItemStack(Material.IRON_NUGGET);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(4);
            itemMeta.setDisplayName(items.getString("deletecancel.title"));
            List<String> lore = items.getStringList("deletecancel.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("mainmenuicon")){
            ItemStack itemStack = new ItemStack(Material.SPECTRAL_ARROW);
            //ItemStack itemStack = new ItemStack(Material.FLINT);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(8);
            itemMeta.setDisplayName(items.getString("mainmenuicon.title"));
            List<String> lore = items.getStringList("mainmenuicon.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("delhomeicon")){
            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(9);
            itemMeta.setDisplayName(items.getString("delhomeicon.title"));
            List<String> lore = items.getStringList("delhomeicon.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("editicon")){
            ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK);
            //ItemStack itemStack = new ItemStack(Material.EGG);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(10);
            itemMeta.setDisplayName(items.getString("editicon.title"));
            List<String> lore = items.getStringList("editicon.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("addhomeicon")){
            ItemStack itemStack = new ItemStack(Material.EMERALD);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(7);
            itemMeta.setDisplayName(items.getString("addhomeicon.title"));
            List<String> lore = items.getStringList("addhomeicon.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("publicselectortrue")){
            ItemStack itemStack = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(items.getString("publicselectortrue.title"));
            List<String> lore = items.getStringList("publicselectortrue.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        else if(path.equalsIgnoreCase("publicselectorfalse")){
            ItemStack itemStack = new ItemStack(Material.RED_CONCRETE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(items.getString("publicselectorfalse.title"));
            List<String> lore = items.getStringList("publicselectorfalse.lore");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }

        return null;
    }


}
