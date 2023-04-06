package me.gorenjec.bunnyeggs.models;

import org.bukkit.entity.Player;

import java.util.Collection;

public class PlayerProfile {
    private Player player;
    private Collection<BunnyEgg> collectedBunnyEggs;
    private int points;

    public PlayerProfile(Player player, Collection<BunnyEgg> collectedBunnyEggs, int points) {
        this.player = player;
        this.collectedBunnyEggs = collectedBunnyEggs;
        this.points = points;
    }

    public boolean addCollectedBunnyEgg(BunnyEgg bunnyEgg) {
        if (!collectedBunnyEggs.contains(bunnyEgg)) {
            collectedBunnyEggs.add(bunnyEgg);
            return true;
        }
        return false;
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
