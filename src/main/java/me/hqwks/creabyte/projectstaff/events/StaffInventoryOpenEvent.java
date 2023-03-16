package me.hqwks.creabyte.projectstaff.events;

import me.hqwks.creabyte.projectstaff.db.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

public class StaffInventoryOpenEvent implements Listener {

    private final me.hqwks.creabyte.projectstaff.db.MySQL MySQL;

    public StaffInventoryOpenEvent(MySQL MySQL) {
        this.MySQL = MySQL;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStaffInventoryOpen(InventoryOpenEvent e) {

        Player p = (Player) e.getPlayer();
        UUID PlayerUUID = p.getUniqueId();
        String PlayerIP = p.getAddress().getAddress().getHostAddress();

        if (MySQL.checkStaffExist(PlayerUUID)) {
            if (!MySQL.checkStaffIP(PlayerUUID, PlayerIP)) {
                e.setCancelled(true);
            }
        }
    }
}
