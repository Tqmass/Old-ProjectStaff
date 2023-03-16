package me.hqwks.creabyte.projectstaff.events;

import me.hqwks.creabyte.projectstaff.db.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class StaffBlockPlaceEvent implements Listener {

    private final me.hqwks.creabyte.projectstaff.db.MySQL MySQL;

    public StaffBlockPlaceEvent(MySQL MySQL) {
        this.MySQL = MySQL;
    }

    @EventHandler
    public void onStaffPlace(BlockPlaceEvent e) {

        Player p = e.getPlayer();
        UUID PlayerUUID = p.getUniqueId();
        String PlayerIP = p.getAddress().getAddress().getHostAddress();

        if (MySQL.checkStaffExist(PlayerUUID)) {
            if (!MySQL.checkStaffIP(PlayerUUID, PlayerIP)) {
                e.setCancelled(true);
            }
        }
    }
}
