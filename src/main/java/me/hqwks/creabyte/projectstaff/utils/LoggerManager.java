package me.hqwks.creabyte.projectstaff.utils;

import me.hqwks.creabyte.projectstaff.ProjectStaff;
import org.bukkit.ChatColor;

public class LoggerManager {

    public static void logEnabledMessage(ProjectStaff ProjectStaff) {

        ProjectStaff.getLogger().info(ChatColor.GREEN + "---------------------------");
        ProjectStaff.getLogger().info(ChatColor.GREEN + "    ProjectStaff - V1.0    ");
        ProjectStaff.getLogger().info(ChatColor.GREEN + "        Made by Hqwks      ");
        ProjectStaff.getLogger().info(ChatColor.BLUE + "          [ENABLED]        ");
        ProjectStaff.getLogger().info(ChatColor.GREEN + "---------------------------");

    }

    public static void logDisabledMessage(ProjectStaff ProjectStaff) {

        ProjectStaff.getLogger().info(ChatColor.GREEN + "---------------------------");
        ProjectStaff.getLogger().info(ChatColor.GREEN + "    ProjectStaff - V1.0    ");
        ProjectStaff.getLogger().info(ChatColor.GREEN + "        Made by Hqwks      ");
        ProjectStaff.getLogger().info(ChatColor.RED + "          [DISABLED]        ");
        ProjectStaff.getLogger().info(ChatColor.GREEN + "---------------------------");


    }

}
