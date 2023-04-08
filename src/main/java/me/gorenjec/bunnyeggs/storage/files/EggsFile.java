package me.gorenjec.bunnyeggs.storage.files;

import me.gorenjec.bunnyeggs.BunnyEggs;
import me.gorenjec.bunnyeggs.cache.InMemoryCache;
import me.gorenjec.bunnyeggs.models.BunnyEgg;
import me.gorenjec.bunnyeggs.models.EggRarity;
import me.gorenjec.bunnyeggs.storage.DataFile;
import me.gorenjec.bunnyeggs.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EggsFile extends DataFile {
    private final BunnyEggs instance;

    public EggsFile(BunnyEggs instance) throws IOException {
        super(instance.getDataFolder().toPath(), "eggs", instance);
        this.instance = instance;
    }

    public Map<Location, BunnyEgg> getBunnyEggs(InMemoryCache inMemoryCache) {
        Map<Location, BunnyEgg> bunnyEggs = new HashMap<>();
        ConfigurationSection section = this.data.getConfigurationSection("");

        for (String key : section.getKeys(false)) {
            String displayName = section.getString(key + ".display-name");
            String eggRarityName = section.getString(key + ".rarity");
            EggRarity eggRarity = inMemoryCache.getEggRarity(eggRarityName);
            ConfigurationSection locSection = section.getConfigurationSection(key + ".loc");
            String worldName = locSection.getString("world");
            World world = Bukkit.getWorld(worldName);
            int x = locSection.getInt("x");
            int y = locSection.getInt("y");
            int z = locSection.getInt("z");
            Location location = new Location(world, x, y, z);
            BunnyEgg bunnyEgg = new BunnyEgg(key, displayName, eggRarity);

            bunnyEggs.put(location, bunnyEgg);
        }

        return bunnyEggs;
    }
}
