package eu.veyno.homesystem.Util;

import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.block.Biome.*;

public class BiomeConverter {

    private static BiomeConverter instance = new BiomeConverter();

    private BiomeConverter(){}

    public static BiomeConverter getInstance() {
        return instance;
    }

    private static final Map<Biome, Integer> biomeIcons = new HashMap<>();

    static {
        Biome[] biomes = {
                Biome.BADLANDS, Biome.BAMBOO_JUNGLE, Biome.BASALT_DELTAS, Biome.BEACH, Biome.BIRCH_FOREST,
                Biome.CHERRY_GROVE, Biome.COLD_OCEAN, Biome.CRIMSON_FOREST, Biome.DARK_FOREST, Biome.DEEP_COLD_OCEAN,
                Biome.DEEP_DARK, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_OCEAN, Biome.DESERT,
                Biome.DRIPSTONE_CAVES, Biome.END_BARRENS, Biome.END_HIGHLANDS, Biome.END_MIDLANDS, Biome.ERODED_BADLANDS,
                Biome.FLOWER_FOREST, Biome.FOREST, Biome.FROZEN_OCEAN, Biome.FROZEN_PEAKS, Biome.FROZEN_RIVER,
                Biome.GROVE, Biome.ICE_SPIKES, Biome.JAGGED_PEAKS, Biome.JUNGLE, Biome.LUKEWARM_OCEAN,
                Biome.LUSH_CAVES, Biome.MANGROVE_SWAMP, Biome.MEADOW, Biome.MUSHROOM_FIELDS, Biome.NETHER_WASTES,
                Biome.OCEAN, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.OLD_GROWTH_PINE_TAIGA, Biome.OLD_GROWTH_SPRUCE_TAIGA,
                Biome.PLAINS, Biome.RIVER, Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.SMALL_END_ISLANDS,
                Biome.SNOWY_BEACH, Biome.SNOWY_PLAINS, Biome.SNOWY_SLOPES, Biome.SNOWY_TAIGA, Biome.SOUL_SAND_VALLEY,
                Biome.SPARSE_JUNGLE, Biome.STONY_PEAKS, Biome.STONY_SHORE, Biome.SUNFLOWER_PLAINS, Biome.SWAMP,
                Biome.TAIGA, Biome.THE_END, Biome.THE_VOID, Biome.WARM_OCEAN, Biome.WARPED_FOREST,
                Biome.WINDSWEPT_FOREST, Biome.WINDSWEPT_GRAVELLY_HILLS, Biome.WINDSWEPT_HILLS, Biome.WINDSWEPT_SAVANNA,
                Biome.WOODED_BADLANDS
        };

        for (int i = 0; i < biomes.length; i++) {
            biomeIcons.put(biomes[i], i + 1);
        }
    }

    public static int getBiomeIcon(Biome biome) {
        return biomeIcons.getOrDefault(biome, -1);
    }

    
}
