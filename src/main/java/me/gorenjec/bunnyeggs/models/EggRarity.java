package me.gorenjec.bunnyeggs.models;

import net.kyori.adventure.text.Component;

public record EggRarity(Component name, int points, double money, int xp) {
}
