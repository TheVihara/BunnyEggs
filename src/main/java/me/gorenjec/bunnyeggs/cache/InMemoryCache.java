package me.gorenjec.bunnyeggs.cache;

import me.gorenjec.bunnyeggs.models.BunnyEgg;
import me.gorenjec.bunnyeggs.models.EggRarity;
import me.gorenjec.bunnyeggs.models.PlayerProfile;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryCache {
    private Map<UUID, PlayerProfile> playerProfiles = new HashMap<>();
    private Map<Location, BunnyEgg> bunnyEggs = new HashMap<>();
    private Map<String, EggRarity> eggRarities = new HashMap<>();

    public InMemoryCache() {
        cache();
    }

    public void cache() {
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

    public EggRarity getEggRarity(String name) {
        return eggRarities.get(name);
    }
}
