package me.gorenjec.bunnyeggs.listener;

import me.gorenjec.bunnyeggs.BunnyEggs;
import me.gorenjec.bunnyeggs.cache.InMemoryCache;
import me.gorenjec.bunnyeggs.models.BunnyEgg;
import me.gorenjec.bunnyeggs.models.EggRarity;
import me.gorenjec.bunnyeggs.models.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class PlayerInteractListener implements Listener {
    private final BunnyEggs instance;
    private final InMemoryCache inMemoryCache;

    public PlayerInteractListener(BunnyEggs instance) {
        this.instance = instance;
        this.inMemoryCache = instance.getInMemoryCache();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerProfile playerProfile = inMemoryCache.getPlayerProfile(uuid);
        Action action = event.getAction();
        EquipmentSlot equipmentSlot = event.getHand();

        if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
            return;
        }

        if (equipmentSlot != EquipmentSlot.HAND) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        Location location = block.getLocation();
        BunnyEgg bunnyEgg = inMemoryCache.getBunnyEgg(location);

        event.setCancelled(true);

        if (!playerProfile.addCollectedBunnyEgg(bunnyEgg)) {
            // send a message saying that the bunny egg has already been collected
            return;
        }

        Component name = bunnyEgg.name();
        EggRarity eggRarity = bunnyEgg.rarity();
        int points = eggRarity.points();
        double money = eggRarity.money();
        int xp = eggRarity.xp();

        playerProfile.addPoints(points);
        player.giveExp(xp);
        // hook into vault and add money
        // send a message saying that the player has collected the bunny egg
    }
}
