package discord.bot.gq.config.db;

import discord.bot.gq.database.ConnectionToDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfigSelection {
    private String roleId = null;
    private String channelId = null;


    public void selectRoleId() {

        try {
            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            String roleId = "SELECT config_value FROM config WHERE config_name = 'id_ping_role'";
            Statement statement = connectionToDB.getConnection().createStatement();
            ResultSet rS = statement.executeQuery(roleId);

            if (rS.next()) {
                this.roleId = rS.getString(1);

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
                this.channelId = rS.getString(1);
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


}