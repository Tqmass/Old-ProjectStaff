package me.hqwks.creabyte.projectstaff.events;

import me.hqwks.creabyte.projectstaff.db.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class StaffCommandPreProcessEvent implements Listener {

    private final me.hqwks.creabyte.projectstaff.db.MySQL MySQL;

    public StaffCommandPreProcessEvent(MySQL MySQL) {
        this.MySQL = MySQL;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onStaffCommandEvent(PlayerCommandPreprocessEvent e) {

        Player p = e.getPlayer();
        UUID PlayerUUID = p.getUniqueId();
        String PlayerIP = p.getAddress().getAddress().getHostAddress();

        if (MySQL.checkStaffExist(PlayerUUID)) {
            if (!MySQL.checkStaffIP(PlayerUUID, PlayerIP)) {
                String PlayerCommandPreProcess = e.getMessage().split(" ")[0];

                if (!(PlayerCommandPreProcess.equals("/sc")
                        || PlayerCommandPreProcess.equals("/ac")
                        || PlayerCommandPreProcess.equals("/stafflogin")
                        || PlayerCommandPreProcess.equals("/staffl"))) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
