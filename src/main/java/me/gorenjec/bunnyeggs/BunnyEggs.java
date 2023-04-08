package me.gorenjec.bunnyeggs;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import me.gorenjec.bunnyeggs.cache.InMemoryCache;
import me.gorenjec.bunnyeggs.listener.PlayerInteractListener;
import me.gorenjec.bunnyeggs.listener.PlayerJoinListener;
import me.gorenjec.bunnyeggs.listener.PlayerQuitListener;
import me.gorenjec.bunnyeggs.storage.SQLStorage;
import me.gorenjec.bunnyeggs.storage.files.EggsFile;
import me.gorenjec.bunnyeggs.storage.files.RaritiesFile;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class BunnyEggs extends JavaPlugin {
    private SQLStorage storage;
    private InMemoryCache inMemoryCache;
    private InventoryManager inventoryManager;
    private BukkitAudiences bukkitAudiences;
    private EggsFile eggsFile;
    private RaritiesFile raritiesFile;

    @Override
    public void onEnable() {
        this.inventoryManager = new InventoryManager(this);
        inventoryManager.invoke();
        this.saveDefaultConfig();
        this.createFiles();
        this.storage = new SQLStorage(this);
        this.inMemoryCache = new InMemoryCache(eggsFile, raritiesFile);
        this.bukkitAudiences = BukkitAudiences.create(this);
        this.registerListeners();
    }

    @Override
    public void onDisable() {
    }

    private void createFiles() {
        try {
            this.eggsFile = new EggsFile(this);
            this.raritiesFile = new RaritiesFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
    }

    public BukkitAudiences getBukkitAudiences() {
        return bukkitAudiences;
    }

    public SQLStorage getStorage() {
        return storage;
    }

    public InMemoryCache getInMemoryCache() {
        return inMemoryCache;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
