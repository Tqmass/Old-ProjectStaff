package me.hqwks.creabyte.projectstaff.db;

import me.hqwks.creabyte.projectstaff.handlers.ConfigHandler;
import me.hqwks.creabyte.projectstaff.utils.TranslateColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    private static Connection DBConnection;
    private final CommandSender Console = Bukkit.getConsoleSender();

    public void connect(ConfigHandler ConfigHandler) {

        FileConfiguration ConfigFile = ConfigHandler.getConfig();

        String Host = ConfigFile.getString("MySQL.Host");
        int Port = ConfigFile.getInt("MySQL.Port");

        String DataBase = ConfigFile.getString("MySQL.DataBase");
        String Username = ConfigFile.getString("MySQL.Username");
        String Password = ConfigFile.getString("MySQL.Password");

        try {
            synchronized (this) {
                try {
                    if (DBConnection != null && !DBConnection.isClosed()) {
                        Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al intentar conectarse a la base de datos."));
                        return;
                    }

                    Class.forName("com.mysql.jdbc.Driver");
                    DBConnection = DriverManager.getConnection("jdbc:mysql://" + Host + ":" + Port + "/" + DataBase, Username, Password);
                    Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &aSe ha conectado correctamente a la base de datos."));

                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return DBConnection;
    }

    public void disconnect() {
        try {
            if (DBConnection != null) {
                DBConnection.close();
                Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &9Base de datos desconectada correctamente."));
            }
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al intentar desconectar la base de datos:" + e.getMessage()));
        }
    }

    public boolean checkStaffExist(UUID StaffUUID) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT COUNT(*) as count FROM staff WHERE uuid = ?");
            statement.setString(1, StaffUUID.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int count = result.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al comprobar la existencia del staff en la base de datos: " + e.getMessage()));
        }
        return false;
    }

    public boolean checkStaffIP(UUID StaffUUID, String StaffIP) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT ip FROM staff WHERE uuid = ?");
            statement.setString(1, StaffUUID.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String storedIP = result.getString("ip");
                return StaffIP.equals(storedIP);
            }
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al comprobar la IP del jugador en la base de datos: " + e.getMessage()));
        }
        return false;
    }

    public boolean getStaffPassword(UUID StaffUUID, String password) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT password FROM staff WHERE uuid = ?");
            statement.setString(1, StaffUUID.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String storedPassword = result.getString("password");
                return password.equals(storedPassword);
            }
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al comprobar la contraseña del staff en la base de datos: " + e.getMessage()));
        }
        return false;
    }

    public String TestGetPassword(UUID StaffUUID) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT password FROM staff WHERE uuid = ?");
            statement.setString(1, StaffUUID.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String storedPassword = result.getString("password");
                return storedPassword;
            }
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al obtener la contraseña del staff de la base de datos: " + e.getMessage()));
        }
        return null;
    }

    public void updateStaffIP(UUID StaffUUID, String StaffIP) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE staff SET ip = ? WHERE uuid = ?");
            statement.setString(1, StaffIP);
            statement.setString(2, StaffUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al actualizar la IP del staff en la base de datos: " + e.getMessage()));
        }
    }

    public String getStaffName(UUID StaffUUID) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT name FROM staff WHERE uuid = ?");
            statement.setString(1, StaffUUID.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getString("name");
            }
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al obtener el nombre del staff de la base de datos: " + e.getMessage()));
        }
        return null;
    }

    public void updateStaffName(UUID StaffUUID, String StaffName) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE staff SET name = ? WHERE uuid = ?");
            statement.setString(1, StaffName);
            statement.setString(2, StaffUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Console.sendMessage(TranslateColor.Code("&8&l[&5&lCreaByte&8&l] &cError al actualizar el nick del staff en la base de datos: " + e.getMessage()));
        }
    }

}