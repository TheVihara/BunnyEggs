package me.gorenjec.bunnyeggs.cache;

import me.gorenjec.bunnyeggs.models.BunnyEgg;
import me.gorenjec.bunnyeggs.models.EggRarity;
import me.gorenjec.bunnyeggs.models.PlayerProfile;
import me.gorenjec.bunnyeggs.storage.files.EggsFile;
import me.gorenjec.bunnyeggs.storage.files.RaritiesFile;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryCache {
    private Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();
    private Map<Location, BunnyEgg> bunnyEggs = new HashMap<>();
    private Map<String, EggRarity> eggRarities = new HashMap<>();

    public InMemoryCache(EggsFile eggsFile, RaritiesFile raritiesFile) {
        cache(eggsFile, raritiesFile);
    }

    public void cache(EggsFile eggsFile, RaritiesFile raritiesFile) {
        this.eggRarities = raritiesFile.getEggRarities();
        this.bunnyEggs = eggsFile.getBunnyEggs(this);
    }

    public void flush() {
    }

    public void addPlayerProfile(UUID uuid, PlayerProfile playerProfile) {
        playerProfiles.put(uuid, playerProfile);
    }

    public void removePlayerProfile(UUID uuid) {
        playerProfiles.remove(uuid);
    }

    public PlayerProfile getPlayerProfile(UUID uuid) {
        return playerProfiles.get(uuid);
    }

    public BunnyEgg getBunnyEgg(Location location) {
        return bunnyEggs.get(location);
    }

    public Location getBunnyEggLocation(BunnyEgg bunnyEgg) {
        for (Location location : bunnyEggs.keySet()) {
            BunnyEgg cachedBunnyEgg = bunnyEggs.get(location);

            if (cachedBunnyEgg.id().equalsIgnoreCase(bunnyEgg.id())) {
                return location;
            }
        }
        return null;
    }

    public EggRarity getEggRarity(String name) {
        return eggRarities.get(name);
    }
}
