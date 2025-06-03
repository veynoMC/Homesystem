package eu.veyno.homesystem.Warp;

import eu.veyno.homesystem.Home.DatabaseManager;
import eu.veyno.homesystem.Home.Home;
import eu.veyno.homesystem.Home.UserInterface;
import eu.veyno.homesystem.Util.ConfigManager;
import eu.veyno.homesystem.Util.IconManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WarpUI implements Listener {

    private File file;
    private FileConfiguration ui;

    private static Inventory[] inventorys;

    public static Plugin plugin;

    private Player player;
    private Home[] homes;
    private String inventoryTitle;

    private static Map<Player, WarpUI> instances = new ConcurrentHashMap<>();

    private WarpUI(){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        init();
    }

    public static WarpUI getInstance(Player player) {
        if(instances.containsKey(player)) return instances.get(player);
        WarpUI w = new WarpUI();
        w.setPlayer(player);
        instances.put(player, w);
        return getInstance(player);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public static void setPlugin(Plugin plugin) {
        WarpUI.plugin = plugin;
    }

    public void init(){
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
        refresh();
    }

    public void refresh() {
        homes = DatabaseManager.getInstance().getPublicHomes().toArray(new Home[0]);
        inventoryTitle = ui.getString("warpinventorytitle");
        int pageCount = (homes.length / 36) + 1;
        inventorys = new Inventory[pageCount];
        int count = 0;
        for (int i = 0; i < inventorys.length; i++) {
            inventorys[i] = Bukkit.createInventory(player, 45, inventoryTitle + " " + (i + 1) + "/" + inventorys.length);
            ItemStack nextPage = ConfigManager.getInstance().getIcon("nextPageArrow", null);
            ItemStack previouspage = ConfigManager.getInstance().getIcon("previousPageArrow", null);
            inventorys[i].setItem(36, previouspage);
            inventorys[i].setItem(44, nextPage);
            for (int j = 0; j < 36; j++) {
                if (count >= homes.length) break;
                int icon = homes[count].getIcon();

                ItemStack homeIcon = IconManager.getInstance().getHomeIcon(icon);

                ItemMeta itemMeta = homeIcon.getItemMeta();
                List<String> lore2= itemMeta.getLore();
                Location location = homes[count].getLocation();
                lore2.set( 3, " "+ ChatColor.RESET + ChatColor.DARK_GRAY + " Z:" + location.getZ());
                lore2.set( 2, " "+ ChatColor.RESET + ChatColor.DARK_GRAY + " Y:" + location.getY());
                lore2.set( 1, " "+ ChatColor.RESET + ChatColor.DARK_GRAY + " X:" + location.getX());
                lore2.set(7, "");
                itemMeta.setDisplayName( ui.getString("homeTitlePrefix") +  homes[count].getTitle() + ui.getString("homeTitleSuffix"));
                itemMeta.setLore(lore2);
                homeIcon.setItemMeta(itemMeta);
                inventorys[i].setItem(j, homeIcon);
                count++;
            }
        }
    }

    public void openMainPage() {
        refresh();
        if (inventorys != null && inventorys.length > 0) {
            player.openInventory(inventorys[0]);
        }
    }

    private Home getHomeBySlot(int slot, int page){
        page--;
        int i = slot+ (page*36);
        return homes[i];
    }

    private void openHomesPage(int page){
        refresh();
        if(inventorys.length>page&&page>=0){
            player.openInventory(inventorys[page]);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getPlayer()==player) {
            boolean isinInventory = false;
            for (Inventory i : inventorys) {
                if (e.getInventory() == i) {
                    isinInventory = true;
                }
            }
            if (isinInventory) {
                e.setCancelled(true);
                String title = ChatColor.stripColor(e.getView().getTitle()).replace(ui.getString("homeinvtitle"), "").trim();
                String[] cleanString = title.split("/");
                String currentPage = cleanString[0].trim().replaceAll("[^0-9]", "");
                int i = Integer.parseInt(currentPage);
                if (e.getSlot() > 35) {
                    if (e.getSlot() == 36) {
                        e.setCancelled(true);
                        if(i>1) {
                            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                            openHomesPage(i - 2);
                        }
                    }
                    if (e.getSlot() == 44) {
                        e.setCancelled(true);
                        if(i<inventorys.length) {
                            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                            openHomesPage(i);
                        }
                    }
                } else {
                    Home home = getHomeBySlot(e.getSlot(), i);
                    if (e.isRightClick()) {
                    } else {
                        home.teleport(player);
                        e.getView().close();
                    }
                }
            }
        }
    }

}
