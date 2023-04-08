package me.gorenjec.bunnyeggs.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TextUtils {
    public static Component getComponent(String text) {
        return MiniMessage.miniMessage().deserialize(legacyToMiniMessage("&", legacyToMiniMessage("§", text)));
    }

    private static String legacyToMiniMessage(String ch, String text) {
        return text
                .replaceAll(ch + "0", "<black>")
                .replaceAll(ch + "1", "<dark_blue>")
                .replaceAll(ch + "2", "<dark_green>")
                .replaceAll(ch + "3", "<dark_aqua>")
                .replaceAll(ch + "4", "<dark_red>")
                .replaceAll(ch + "5", "<dark_purple>")
                .replaceAll(ch + "6", "<gold>")
                .replaceAll(ch + "7", "<grey>")
                .replaceAll(ch + "8", "<dark_grey>")
                .replaceAll(ch + "9", "<blue>")
                .replaceAll(ch + "a", "<green>")
                .replaceAll(ch + "b", "<aqua>")
                .replaceAll(ch + "c", "<red>")
                .replaceAll(ch + "d", "<light_purple>")
                .replaceAll(ch + "e", "<yellow>")
                .replaceAll(ch + "f", "<white>")
                .replaceAll(ch + "m", "<st>")
                .replaceAll(ch + "k", "<obf>")
                .replaceAll(ch + "o", "<i>")
                .replaceAll(ch + "l", "<b>")
                .replaceAll(ch + "r", "<r>")
                .replaceAll("/" + ch + "#([0-9a-fA-F]{6})/g", "<#$1>");
    }
}
