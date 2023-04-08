package me.gorenjec.bunnyeggs.listener;

import me.gorenjec.bunnyeggs.BunnyEggs;
import me.gorenjec.bunnyeggs.cache.InMemoryCache;
import me.gorenjec.bunnyeggs.models.PlayerProfile;
import me.gorenjec.bunnyeggs.storage.SQLStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {
    private final BunnyEggs instance;
    private final InMemoryCache inMemoryCache;
    private final SQLStorage storage;

    public PlayerQuitListener(BunnyEggs instance) {
        this.instance = instance;
        this.inMemoryCache = instance.getInMemoryCache();
        this.storage = instance.getStorage();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerProfile playerProfile = inMemoryCache.getPlayerProfile(uuid);

        playerProfile.flush(storage);
        inMemoryCache.removePlayerProfile(uuid);
    }
}
