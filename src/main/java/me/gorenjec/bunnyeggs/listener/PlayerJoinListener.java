package me.gorenjec.bunnyeggs.listener;

import me.gorenjec.bunnyeggs.BunnyEggs;
import me.gorenjec.bunnyeggs.cache.InMemoryCache;
import me.gorenjec.bunnyeggs.models.BunnyEgg;
import me.gorenjec.bunnyeggs.models.PlayerProfile;
import me.gorenjec.bunnyeggs.storage.SQLStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerJoinListener implements Listener {
    private final BunnyEggs instance;
    private final InMemoryCache inMemoryCache;
    private final SQLStorage storage;

    public PlayerJoinListener(BunnyEggs instance) {
        this.instance = instance;
        this.inMemoryCache = instance.getInMemoryCache();
        this.storage = instance.getStorage();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Logger logger = instance.getLogger();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Map<Location, BunnyEgg> bunnyEggs = storage.getBunnyEggs(uuid);
        Collection<BunnyEgg> bunnyEggsCollection = bunnyEggs.values();
        int points = storage.getPoints(uuid);
        PlayerProfile playerProfile = new PlayerProfile(
                player,
                bunnyEggsCollection,
                points
        );

        inMemoryCache.addPlayerProfile(uuid, playerProfile);
        logger.log(Level.INFO, "Cached player profile");
    }
}
