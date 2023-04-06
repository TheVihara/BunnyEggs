package me.gorenjec.bunnyeggs;

import me.gorenjec.bunnyeggs.cache.InMemoryCache;
import org.bukkit.plugin.java.JavaPlugin;

public final class BunnyEggs extends JavaPlugin {
    private InMemoryCache inMemoryCache;

    @Override
    public void onEnable() {
        this.inMemoryCache = new InMemoryCache();
    }

    @Override
    public void onDisable() {
    }

    public InMemoryCache getInMemoryCache() {
        return inMemoryCache;
    }
}
