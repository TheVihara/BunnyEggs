package me.gorenjec.bunnyeggs.storage.files;

import me.gorenjec.bunnyeggs.BunnyEggs;
import me.gorenjec.bunnyeggs.models.EggRarity;
import me.gorenjec.bunnyeggs.storage.DataFile;
import me.gorenjec.bunnyeggs.util.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RaritiesFile extends DataFile {
    private final BunnyEggs instance;

    public RaritiesFile(BunnyEggs instance) throws IOException {
        super(instance.getDataFolder().toPath(), "rarities", instance);
        this.instance = instance;
    }

    public Map<String, EggRarity> getEggRarities() {
        Map<String, EggRarity> eggRarities = new HashMap<>();
        ConfigurationSection section = this.data.getConfigurationSection("");

        for (String key : section.getKeys(false)) {
            String displayName = section.getString(key + ".display-name");
            int points = section.getInt(key + ".points");
            int xp = section.getInt(key + ".xp");
            double money = section.getDouble(key + ".money");
            EggRarity eggRarity = new EggRarity(displayName, points, money, xp);

            eggRarities.put(key, eggRarity);
        }

        return eggRarities;
    }
}
