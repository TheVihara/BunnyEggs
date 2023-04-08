package me.gorenjec.bunnyeggs.guis;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import me.gorenjec.bunnyeggs.BunnyEggs;
import me.gorenjec.bunnyeggs.models.BunnyEgg;
import me.gorenjec.bunnyeggs.models.PlayerProfile;
import me.gorenjec.bunnyeggs.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EggsGui {
    private final BunnyEggs instance;
    private RyseInventory gui;

    public EggsGui(BunnyEggs instance) {
        this.instance = instance;
    }

    public void create(PlayerProfile playerProfile) {
        this.gui = RyseInventory.builder()
                .title("Eggs Collection (" + playerProfile.getCollectedBunnyEggCount() + ")")
                .size(27)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {
                        ItemStack fillerItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                                .displayName("&r", true)
                                .build();

                        for (BunnyEgg bunnyEgg : playerProfile.getCollectedBunnyEggs()) {
                            ItemStack eggItem = new ItemBuilder(Material.PLAYER_HEAD)
                                    .displayName(bunnyEgg.name(), true)
                                    .lore(true, bunnyEgg.rarity().name(), "&r", "&a&oCollected")
                                    .build();

                            contents.add(eggItem);
                        }

                        contents.fillBorders(fillerItem);
                    }
                }).build(instance);
    }

    public void show(Player player) {
        gui.open(player);
    }
}
