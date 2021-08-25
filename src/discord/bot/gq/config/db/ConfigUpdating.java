package discord.bot.gq.config.db;

import discord.bot.gq.config.command.UpdatingChannel;
import discord.bot.gq.config.command.UpdatingRole;
import discord.bot.gq.database.ConnectionToDB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfigUpdating {

    public void updateRoleId() {

        try {

            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            String roleId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_role'";
            PreparedStatement updatePStatement = connectionToDB.getConnection().prepareStatement(roleId);
            updatePStatement.setString(1, UpdatingRole.roleId);

            updatePStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public void updateChannelId() {

        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String channelId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_channel'";
            PreparedStatement updatePStatement = db.getConnection().prepareStatement(channelId);

            updatePStatement.setString(1, UpdatingChannel.channelId);
            updatePStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }
}
