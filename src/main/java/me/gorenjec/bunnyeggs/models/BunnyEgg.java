package me.gorenjec.bunnyeggs.models;

import net.kyori.adventure.text.Component;

public record BunnyEgg(String id, Component name, EggRarity rarity) {
}
