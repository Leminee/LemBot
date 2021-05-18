package discord.bot.gq.config.db;

import discord.bot.gq.database.ConnectionToDB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfigUpdating {

    public void updateRoleId() {

        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();
            ConfigSelection configSelection = new ConfigSelection();
            String roleId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_role'";
            PreparedStatement updatePStatement = db.getConnection().prepareStatement(roleId);
            updatePStatement.setString(1, configSelection.getRoleId());

            updatePStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public void updateChannelId() {

        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();
            ConfigSelection configSelection = new ConfigSelection();
            String channelId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_channel'";
            PreparedStatement updatePStatement = db.getConnection().prepareStatement(channelId);
            updatePStatement.setString(1, configSelection.getChannelId());

            updatePStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
}
