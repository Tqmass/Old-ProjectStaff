package me.hqwks.creabyte.projectstaff;

import me.hqwks.creabyte.projectstaff.commands.StaffLoginCommand;
import me.hqwks.creabyte.projectstaff.db.MySQL;
import me.hqwks.creabyte.projectstaff.events.*;
import me.hqwks.creabyte.projectstaff.handlers.ConfigHandler;
import me.hqwks.creabyte.projectstaff.utils.LoggerManager;
import me.hqwks.creabyte.projectstaff.utils.command.other.CommandRegisterProvider;
import me.hqwks.creabyte.projectstaff.utils.command.other.CommandRegistry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProjectStaff extends JavaPlugin {

    private me.hqwks.creabyte.projectstaff.db.MySQL MySQL;
    private ConfigHandler ConfigHandler;
    private FileConfiguration File;

    @Override
    public void onEnable() {

        ConfigHandler = new ConfigHandler(this);
        ConfigHandler.reloadConfig();

        MySQL = new MySQL();
        MySQL.connect(ConfigHandler);

        CommandRegistry CommandRegistry = new CommandRegisterProvider();
        CommandRegistry.register(
                new StaffLoginCommand(MySQL)
        );

        registerListeners(
                (Listener)new StaffMoveEvent(MySQL),
                (Listener)new StaffInteractEvent(MySQL),
                (Listener)new StaffItemHeldEvent(MySQL),
                (Listener)new StaffInventoryOpenEvent(MySQL),
                (Listener)new StaffCommandPreProcessEvent(MySQL),
                (Listener)new StaffBlockBreakEvent(MySQL),
                (Listener)new StaffBlockPlaceEvent(MySQL),
                (Listener)new StaffAsyncChatEvent(MySQL),
                (Listener)new StaffJoinEvent(MySQL)
        );

        saveConfig();
        LoggerManager.logEnabledMessage(this);

    }

    @Override
    public void onDisable() {

        ConfigHandler.saveConfig();
        MySQL.disconnect();
        LoggerManager.logDisabledMessage(this);

    }

    private void registerListeners(Listener... listeners) {
        byte b;
        int i;
        Listener[] arrayOfListener;
        for (i = (arrayOfListener = listeners).length, b = 0; b < i; ) {
            Listener listener = arrayOfListener[b];
            getServer().getPluginManager().registerEvents(listener, (Plugin) this);
            b++;
        }
    }

}
