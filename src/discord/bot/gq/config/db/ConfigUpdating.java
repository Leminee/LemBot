package discord.bot.gq.config.db;

import discord.bot.gq.config.command.UpdatingChannel;
import discord.bot.gq.config.command.UpdatingRole;
import discord.bot.gq.database.ConnectionToDB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfigUpdating {

    public void updateRoleId() {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String roleId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_role'";

        try(PreparedStatement updatePStatement = connectionToDB.getConnection().prepareStatement(roleId)) {

            updatePStatement.setString(1, UpdatingRole.roleId);

            updatePStatement.executeUpdate();


        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

        }
    }

    public void updateBotCommandsChannelId() {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String channelId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_channel'";

        try (PreparedStatement updatePStatement = connectionToDB.getConnection().prepareStatement(channelId)){


            updatePStatement.setString(1, UpdatingChannel.channelId);
            updatePStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

        }
    }
}
