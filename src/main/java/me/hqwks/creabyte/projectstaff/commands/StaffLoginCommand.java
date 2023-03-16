package me.hqwks.creabyte.projectstaff.commands;

import me.hqwks.creabyte.projectstaff.db.MySQL;
import me.hqwks.creabyte.projectstaff.utils.TranslateColor;
import me.hqwks.creabyte.projectstaff.utils.command.Command;
import me.hqwks.creabyte.projectstaff.utils.command.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@CommandInfo(name = "stafflogin", aliases = {"staffl"}, playerOnly = false)

public class StaffLoginCommand extends Command {

    private final me.hqwks.creabyte.projectstaff.db.MySQL MySQL;
    private HashMap<UUID, Integer> StaffLoginAttemps = new HashMap<>();

    public StaffLoginCommand(MySQL MySQL) {
        this.MySQL = MySQL;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (!p.hasPermission("projectstaff.stafflogin")) {
                return true;
            }

            if (args.length != 1) {
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cUso correcto: &e&o/stafflogin <contraseña>"));
                return true;
            }

            UUID PlayerUUID = p.getUniqueId();
            String PlayerIP = p.getAddress().getAddress().getHostAddress();
            String PlayerName = p.getName();
            String PlayerPassword = args[0];

            int Attemps = StaffLoginAttemps.getOrDefault(PlayerUUID, 0);

            if (MySQL.getStaffPassword(PlayerUUID, PlayerPassword)) {
                MySQL.updateStaffIP(PlayerUUID, PlayerIP);
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &a¡Contraseña correcta, bienvenido de vuelta &e" + PlayerName + "&a!"));
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &b¡La localización de tu cuenta, ha sido actualizada en la base de datos!"));
                StaffLoginAttemps.remove(PlayerUUID);
            } else {
                Attemps++;
                StaffLoginAttemps.put(PlayerUUID, Attemps);
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &c¡Contraseña incorrecta!"));
                p.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &b¡Vuelve a intentarlo nuevamente!"));

                if (Attemps == 3) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + p.getName() + " &4&l¡No has pasado la prueba de seguridad, contacta con un superior!");
                }
            }
        }
        return true;
    }
}