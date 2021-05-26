package discord.bot.gq.config.db;

import discord.bot.gq.database.ConnectionToDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfigSelection {
    private static String roleId = null;
    private static String channelId = null;


    public void selectRoleId() {

        try {
            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String roleId = "SELECT config_value FROM config WHERE config_name = 'id_ping_role'";
            Statement statement = db.getConnection().createStatement();
            ResultSet rS = statement.executeQuery(roleId);

            if (rS.next()) {
                ConfigSelection.roleId = rS.getString(1);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
    public void selectChannelId() {
        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String channelId = "SELECT config_value FROM config WHERE config_name = 'id_ping_channel'";
            Statement statement = db.getConnection().createStatement();
            ResultSet rS = statement.executeQuery(channelId);

            if (rS.next()) {
                ConfigSelection.channelId = rS.getString(1);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public String getRoleId() {
        return roleId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setRoleId(String roleId) {
        ConfigSelection.roleId = roleId;
    }

    public void setChannelId(String channelId) {
        ConfigSelection.channelId = channelId;
    }

}