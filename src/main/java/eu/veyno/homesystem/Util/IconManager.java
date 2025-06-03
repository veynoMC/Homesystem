package eu.veyno.homesystem.Util;

import eu.veyno.homesystem.Home.Home;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class IconManager {
    private static IconManager instance = new IconManager();
    private List<String> lore = new ArrayList<>();
    private YamlConfiguration config;

    private IconManager(){
        init();
    }

    public void init(){
        lore = new ArrayList<>();
        lore.add("§r§l§f----------");
        lore.add("");
        lore.add("");
        lore.add("");
        lore.add("§r§l§f----------");
        lore.add("§r§l§e§oLinksklick zum teleportieren");
        lore.add(" ");
        lore.add("§r§l§b§oRechtsklick zum bearbeiten");
    }

    public static IconManager getInstance() {
        return instance;
    }

    public ItemStack getHomeIcon(Home home){
        return getHomeIcon(home.getIcon());
    }

    public void createDefaultConfig(){

    }

    public ItemStack getHomeIcon(int i){
        init();
        ItemStack itemStack = getIcon(i);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(i);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static final Material[] BIOME_MATERIALS = {
            Material.TERRACOTTA,          // 1 - Badlands
            Material.BAMBOO,              // 2 - Bamboo Jungle
            Material.BASALT,              // 3 - Basalt Deltas
            Material.SAND,                // 4 - Beach
            Material.BIRCH_LOG,           // 5 - Birch Forest
            Material.CHERRY_LEAVES,       // 6 - Cherry Grove
            Material.SEA_LANTERN,         // 7 - Cold Ocean
            Material.CRIMSON_NYLIUM,      // 8 - Crimson Forest
            Material.DARK_OAK_LOG,        // 9 - Dark Forest
            Material.PRISMARINE,          // 10 - Deep Cold Ocean
            Material.REINFORCED_DEEPSLATE,// 11 - Deep Dark
            Material.ICE,                 // 12 - Deep Frozen Ocean
            Material.TUBE_CORAL_BLOCK,    // 13 - Deep Lukewarm Ocean
            Material.DARK_PRISMARINE,     // 14 - Deep Ocean
            Material.SANDSTONE,           // 15 - Desert
            Material.POINTED_DRIPSTONE,   // 16 - Dripstone Caves
            Material.END_STONE,           // 17 - End Barrens
            Material.END_STONE_BRICKS,    // 18 - End Highlands
            Material.PURPUR_BLOCK,        // 19 - End Midlands
            Material.RED_SAND,            // 20 - Eroded Badlands
            Material.DANDELION,    // 21 - Flower Forest
            Material.OAK_LEAVES,          // 22 - Forest
            Material.PACKED_ICE,          // 23 - Frozen Ocean
            Material.SNOW_BLOCK,          // 24 - Frozen Peaks
            Material.BLUE_ICE,            // 25 - Frozen River
            Material.SPRUCE_LEAVES,       // 26 - Grove
            Material.ICE,                 // 27 - Ice Spikes
            Material.SNOWBALL,            // 28 - Jagged Peaks
            Material.JUNGLE_LOG,          // 29 - Jungle
            Material.SEAGRASS,            // 30 - Lukewarm Ocean
            Material.GLOW_BERRIES,        // 31 - Lush Caves
            Material.MANGROVE_ROOTS,      // 32 - Mangrove Swamp
            Material.CORNFLOWER,          // 33 - Meadow
            Material.MYCELIUM,            // 34 - Mushroom Fields
            Material.NETHERRACK,          // 35 - Nether Wastes
            Material.WATER_BUCKET,        // 36 - Ocean
            Material.BIRCH_LOG,           // 37 - Old Growth Birch Forest
            Material.SPRUCE_LOG,          // 38 - Old Growth Pine Taiga
            Material.SPRUCE_LOG,          // 39 - Old Growth Spruce Taiga
            Material.GRASS_BLOCK,         // 40 - Plains
            Material.WATER_BUCKET,        // 41 - River
            Material.ACACIA_LOG,          // 42 - Savanna
            Material.ACACIA_LEAVES,       // 43 - Savanna Plateau
            Material.END_STONE,           // 44 - Small End Islands
            Material.SNOW,                // 45 - Snowy Beach
            Material.SNOW_BLOCK,          // 46 - Snowy Plains
            Material.POWDER_SNOW,         // 47 - Snowy Slopes
            Material.SPRUCE_LOG,          // 48 - Snowy Taiga
            Material.SOUL_SAND,           // 49 - Soul Sand Valley
            Material.JUNGLE_LEAVES,       // 50 - Sparse Jungle
            Material.COBBLESTONE,         // 51 - Stony Peaks
            Material.GRAVEL,              // 52 - Stony Shore
            Material.SUNFLOWER,           // 53 - Sunflower Plains
            Material.MUD,                 // 54 - Swamp
            Material.SPRUCE_LEAVES,       // 55 - Taiga
            Material.END_STONE,           // 56 - The End
            Material.BARRIER,             // 57 - The Void
            Material.BRAIN_CORAL_BLOCK,   // 58 - Warm Ocean
            Material.WARPED_NYLIUM,       // 59 - Warped Forest
            Material.MOSSY_COBBLESTONE,   // 60 - Windswept Forest
            Material.GRAVEL,              // 61 - Windswept Gravelly Hills
            Material.STONE,               // 62 - Windswept Hills
            Material.ACACIA_LOG,          // 63 - Windswept Savanna
            Material.RED_SANDSTONE        // 64 - Wooded Badlands
    };

    public static ItemStack getIcon(int i) {
        if (i < 1 || i > BIOME_MATERIALS.length) {
            return new ItemStack(Material.BARRIER); // Default for out-of-range values
        }
        return new ItemStack(BIOME_MATERIALS[i - 1]);
    }

}
