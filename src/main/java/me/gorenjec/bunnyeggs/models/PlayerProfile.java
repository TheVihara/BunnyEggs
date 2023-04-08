package me.gorenjec.bunnyeggs.models;

import me.gorenjec.bunnyeggs.storage.SQLStorage;
import me.gorenjec.bunnyeggs.util.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerProfile {
    private Player player;
    private Collection<BunnyEgg> collectedBunnyEggs;
    private Map<Location, BunnyEgg> newlyCollectedBunnyEggs = new HashMap<>();
    private int points;

    public PlayerProfile(Player player, Collection<BunnyEgg> collectedBunnyEggs, int points) {
        this.player = player;
        this.collectedBunnyEggs = collectedBunnyEggs;
        this.points = points;
    }

    public void flush(SQLStorage storage) {
        for (Map.Entry<Location, BunnyEgg> bunnyEgg : newlyCollectedBunnyEggs.entrySet()) {
            storage.insertCollectedEgg(this, bunnyEgg.getKey(), bunnyEgg.getValue());
        }

        collectedBunnyEggs.addAll(newlyCollectedBunnyEggs.values());
        newlyCollectedBunnyEggs.clear();
    }

    public boolean addCollectedBunnyEgg(Location location, BunnyEgg bunnyEgg) {
        if (!collectedBunnyEggs.contains(bunnyEgg) && !newlyCollectedBunnyEggs.containsValue(bunnyEgg)) {
            newlyCollectedBunnyEggs.put(location, bunnyEgg);
            return true;
        }
        return false;
    }

    public void sendMessage(Audience audience, String text) {
        audience.sendMessage(TextUtils.getComponent(text));
    }

    public void removeCollectedBunnyEgg(BunnyEgg bunnyEgg) {
        collectedBunnyEggs.remove(bunnyEgg);
    }

    public void addPoints(int points) {
        this.points = this.points + points;
    }

    public void removePoints(int points) {
        this.points = this.points - points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Player getPlayer() {
        return player;
    }

    public Collection<BunnyEgg> getCollectedBunnyEggs() {
        return collectedBunnyEggs;
    }

    public int getCollectedBunnyEggCount() {
        return collectedBunnyEggs.size();
    }

    public int getPoints() {
        return points;
    }
}
