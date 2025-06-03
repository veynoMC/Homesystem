package eu.veyno.homesystem.Home;

import eu.veyno.homesystem.Util.BiomeConverter;
import eu.veyno.homesystem.Util.ConfigManager;
import eu.veyno.homesystem.Util.IconManager;
import eu.veyno.homesystem.Util.PermissionManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserInterface implements Listener {
    
    private static Map<Player, UserInterface> instances = new ConcurrentHashMap<>();

    public static Plugin plugin;

    private Inventory currentEditInventory;
    private Inventory[] inventorys;
    private Inventory deleteGUI;
    private Inventory publicGUI;
    private Home currentlyDeleting;

    private Player player;
    private Home[] homes;
    private String inventoryTitle;

    private File file;
    private FileConfiguration ui;
    private File file1;
    private FileConfiguration icons;
    private File file2;
    private FileConfiguration homeIcons;

    private UserInterface(Player player) {
        this.player = player;
        this.inventoryTitle = player.getName() + " Homes übersicht";
        instances.put(player, this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        init();
    }

    public static UserInterface getInstance(Player player) {
        if(instances.containsKey(player)){
            return instances.get(player);
        }
        else{
            UserInterface userInterface = new UserInterface(player);
            instances.put(player, userInterface);
            return userInterface;
        }
    }

    public static void setPlugin(Plugin plugin) {
        UserInterface.plugin = plugin;
    }

    public Player getPlayer() {
        return player;
    }

    private void init(){
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
        file1 = new File(discordConfigsFolder, "icons.yml");
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        icons = YamlConfiguration.loadConfiguration(file1);
        file2 = new File(discordConfigsFolder, "homeicons.yml");
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        homeIcons = YamlConfiguration.loadConfiguration(file2);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, p -> {
            refresh();
        });
    }

    public void refresh() {

        inventoryTitle = ui.getString("homeinvtitle");
        homes = DatabaseManager.getInstance().getAllHomesOfPlayer(player.getUniqueId().toString()).toArray(new Home[0]);
        int pageCount = (homes.length / 36) + 1;
        inventorys = new Inventory[pageCount];
        int count = 0;
        for (int i = 0; i < inventorys.length; i++) {
            inventorys[i] = Bukkit.createInventory(player, 45, inventoryTitle + " " + (i + 1) + "/" + inventorys.length);
            ItemStack nextPage = ConfigManager.getInstance().getIcon("nextPageArrow", icons);
            ItemStack previouspage = ConfigManager.getInstance().getIcon("previousPageArrow", icons);
            ItemStack createHome = ConfigManager.getInstance().getIcon("addhomeicon" , icons);
            inventorys[i].setItem(40, createHome);
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
                lore2.add("§f----------");
                lore2.add(ui.getString("worldprefix") + location.getWorld().getName());
                lore2.add(ui.getString("biomeprefix") + location.getWorld().getBiome(location).name());
                lore2.add("§f----------");
                itemMeta.setDisplayName( ui.getString("homeTitlePrefix") +  homes[count].getTitle() + ui.getString("homeTitleSuffix"));
                itemMeta.setLore(lore2);
                homeIcon.setItemMeta(itemMeta);
                inventorys[i].setItem(j, homeIcon);
                count++;
            }
        }
    }

    private void openHomeControlls(Home home){
        String homeTitlePrefix = ui.getString("homecontrollprefix");
        currentEditInventory = Bukkit.createInventory(player, 9, homeTitlePrefix + home.getTitle());

        ItemStack editicon = ConfigManager.getInstance().getIcon("editicon", icons);
        currentEditInventory.setItem(4, editicon);
        ItemStack deleteIcon = ConfigManager.getInstance().getIcon("delhomeicon", icons);
        currentEditInventory.setItem(8, deleteIcon);
        ItemStack mainmenuicon = ConfigManager.getInstance().getIcon("mainmenuicon", icons);
        currentEditInventory.setItem(0, mainmenuicon);
        ItemStack publicIcon = ConfigManager.getInstance().getIcon("publicselectortrue", null);
        ItemStack privatIcon = ConfigManager.getInstance().getIcon("publicselectorfalse", null);
        if(home.isPublic()){
            currentEditInventory.setItem(2, publicIcon);
        }
        else {
            currentEditInventory.setItem(2, privatIcon);
        }

        player.openInventory(currentEditInventory);
    }

    public void openMainPage() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, p -> {
        refresh();
        if (inventorys != null && inventorys.length > 0) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.openInventory(inventorys[0]);
            });
        }
        });
    }

    private Home getHomeBySlot(int slot, int page){
        page--;
        int i = slot+ (page*36);
        return homes[i];
    }

    private void openHomesPage(int page){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, p -> {
            refresh();
            if(inventorys.length>page&&page>=0){
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.openInventory(inventorys[page]);
                });
            }
        });
    }

    public void openRenameGUI(){
        openAnvilInput(player);
    }

    private void opendelGUI(){
        deleteGUI = Bukkit.createInventory(player, 9, ui.getString("delconfirmguititle") + currentlyDeleting.getTitle());

        ItemStack del = ConfigManager.getInstance().getIcon("deleteconfirm", icons);
        deleteGUI.setItem(6, del);
        deleteGUI.setItem(7, del);
        deleteGUI.setItem(8, del);
        deleteGUI.setItem(5, del);
        ItemStack cancel = ConfigManager.getInstance().getIcon("deletecancel", icons);
        deleteGUI.setItem(0, cancel);
        deleteGUI.setItem(1, cancel);
        deleteGUI.setItem(2, cancel);
        deleteGUI.setItem(3, cancel);
        ItemStack mainmenuicon = ConfigManager.getInstance().getIcon("mainmenuicon", icons);
        deleteGUI.setItem(4 , mainmenuicon);

        player.openInventory(deleteGUI);
    }

    private void openAnvilInput(Player player) {
        new AnvilGUI.Builder()
                .onClick((p, text) -> {
                    renameCurrentHome(text.getText());
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    return AnvilGUI.Response.close();
                })
                .text("Hier eingeben!")
                .title(ui.getString("renameguititle"))
                .plugin(plugin)
                .open(player);
    }

    private void openNameInput(Player player, Home home) {
        new AnvilGUI.Builder()
                .onClick((p, text) -> {
                    home.setTitle(text.getText());
                    saveHome(home, player);
                    return AnvilGUI.Response.close();
                })
                .text("Hier eingeben!")
                .title(ui.getString("setnameguititle"))
                .plugin(plugin)
                .open(player);
    }

    private void renameCurrentHome(String text){
        currentlyDeleting.setTitle(text);
        DatabaseManager.getInstance().delHome(currentlyDeleting.getHomeID());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            DatabaseManager.getInstance().saveHome(player, currentlyDeleting);
        });
    }

    public void saveHome(Home home, Player player){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
            Biome b1 = player.getWorld().getBiome(player.getLocation());
            home.setIcon(BiomeConverter.getInstance().getBiomeIcon(b1));
            if (DatabaseManager.getInstance().saveHome(player, home)) {
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                player.sendMessage(ui.getString("prefix") + ui.getString("savedsuccessfully"));
            } else {
                player.sendMessage(ui.getString("prefix") + ui.getString("savedunsuccessfully"));
            }
            refresh();
        });
    }

    private void onPublicButtonpress(){
        if(currentlyDeleting.isPublic()){
            currentlyDeleting.setPublic(false);
            DatabaseManager.getInstance().delHome(currentlyDeleting.getHomeID());
            DatabaseManager.getInstance().saveHome(player, currentlyDeleting);
            openHomeControlls(currentlyDeleting);
        }else if(DatabaseManager.getInstance().getPlayerPublicHomeCount(player) < PermissionManager.getMaxPublicHomesOfPlayer(player)) {
            currentlyDeleting.setPublic(true);
            DatabaseManager.getInstance().delHome(currentlyDeleting.getHomeID());
            DatabaseManager.getInstance().saveHome(player, currentlyDeleting);
            openHomeControlls(currentlyDeleting);
        }
        else{
            player.sendMessage(ui.getString("prefix") + ui.getString("maxpublichomesreached"));
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getSlot()<0) return;
        if(e.getView().getPlayer()==player) {
            if (e.getInventory() == currentEditInventory) {
                if (e.getSlot() == 8) {
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    opendelGUI();
                }
                else if(e.getSlot()==0){
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    openHomesPage(0);
                }
                else if(e.getSlot()==4){
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    openRenameGUI();
                }
                else if(e.getSlot()==2){
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->{
                        onPublicButtonpress();
                    });
                }
                e.setCancelled(true);
            }
            if (e.getInventory() == deleteGUI) {
                e.setCancelled(true);
                if (e.getSlot() > 4 && e.getSlot()<10) {
                    if(DatabaseManager.getInstance().delHome(currentlyDeleting.getHomeID())){
                        player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        player.sendMessage(ui.getString("prefix") + ui.getString("homedeletesuccess"));
                    }
                    openHomesPage(0);
                }
                else if(e.getSlot()==4){
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    deleteGUI = null;
                    openHomesPage(0);
                }
                else if(e.getSlot()<4&&e.getSlot()>=0){
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    deleteGUI = null;
                    e.getView().close();
                }
            }
            //wenn der Spieler sich auf einer überischtsseite befindet:
            boolean isinInventory = false;
            for (Inventory i : inventorys) {
                if (e.getInventory() == i) {
                    isinInventory = true;
                }
            }
            if (isinInventory) {
                e.setCancelled(true);
                String title = e.getView().getTitle().replace(ui.getString("homeinvtitle"), "");
                String[] cleanString = title.split("/");


                String currentPage = cleanString[0].trim();
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
                    if (e.getSlot() == 40) {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        e.setCancelled(true);
                        e.getView().close();
                        Home home = new Home(e.getView().getPlayer().getLocation(), "Home");
                        openNameInput(player, home);
                    }
                } else {
                    Home home = getHomeBySlot(e.getSlot(), i);
                    if (e.isRightClick()) {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        openHomeControlls(home);
                        currentlyDeleting = home;
                    } else {
                        home.teleport(player);
                        e.getView().close();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        instances.remove(player);
    }
}