package discord.bot.gq.db.config;

import discord.bot.gq.db.ConnectionToDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfigSelection {
    public static String roleId = null;
    public static String channelId = null;


    public static void selectRoleId() {
        try {
            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String selectRoleId = "SELECT config_value FROM config WHERE config_name = 'id_ping_role'";
            Statement statement = db.connection.createStatement();
            ResultSet rS = statement.executeQuery(selectRoleId);

            if (rS.next()) {
                roleId = rS.getString(1);

            }
        }catch (SQLException e) {
            e.getMessage();

        }

    }

    public static void selectChannelId(){
        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String selectChannelId = "SELECT config_value FROM config WHERE config_name = 'id_ping_channel'";
            Statement statement = db.connection.createStatement();
            ResultSet rS = statement.executeQuery(selectChannelId);

            if (rS.next()) {
                channelId = rS.getString(1);

            }

        } catch (SQLException e) {
            e.getMessage();
        }

    }


}