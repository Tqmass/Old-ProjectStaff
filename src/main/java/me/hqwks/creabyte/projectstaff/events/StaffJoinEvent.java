package me.hqwks.creabyte.projectstaff.events;

import me.hqwks.creabyte.projectstaff.db.MySQL;
import me.hqwks.creabyte.projectstaff.utils.TranslateColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class StaffJoinEvent implements Listener {

    private final me.hqwks.creabyte.projectstaff.db.MySQL MySQL;

    public StaffJoinEvent(MySQL MySQL) {
        this.MySQL = MySQL;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStaffJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        UUID PlayerUUID = p.getUniqueId();
        String PlayerIP = p.getAddress().getAddress().getHostAddress();
        String PlayerName = p.getName();

        if (MySQL.checkStaffExist(PlayerUUID)) {
            if (MySQL.checkStaffIP(PlayerUUID, PlayerIP)) {
                if (!MySQL.getStaffName(PlayerUUID).equals(PlayerName)) {
                    MySQL.updateStaffName(PlayerUUID, PlayerName);
                }
            } else {
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &4&l¡SE HA DETECTADO UN CAMBIO DE IP!"));
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &4&lPOR FAVOR, VERIFICA TU ACCESO USANDO:"));
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &b/stafflogin <contraseña>"));
            }
        }
    }
}