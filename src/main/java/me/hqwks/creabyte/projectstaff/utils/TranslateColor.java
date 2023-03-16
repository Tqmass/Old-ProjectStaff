package me.hqwks.creabyte.projectstaff.utils;

import org.bukkit.ChatColor;

public class TranslateColor {

    public static String Code(String PlayerMessage){
        return ChatColor.translateAlternateColorCodes('&', PlayerMessage);
    }

}
