package me.hqwks.creabyte.projectstaff.events;

import me.hqwks.creabyte.projectstaff.db.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class StaffAsyncChatEvent implements Listener {

    private final MySQL MySQL;

    public StaffAsyncChatEvent(MySQL MySQL) {
        this.MySQL = MySQL;
    }

    @EventHandler
    public void onStaffAsyncChat(AsyncPlayerChatEvent e) {

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
