package discord.bot.gq.config.db;

import discord.bot.gq.config.command.UpdatingChannel;
import discord.bot.gq.config.command.UpdatingRole;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfigUpdating {

    public void updateRoleId() {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String roleId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_role'";

        try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(roleId)) {

            preparedStatement.setString(1, UpdatingRole.roleId);

            preparedStatement.executeUpdate();


        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

        }
    }

    public void updateBotCommandsChannelId() {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String channelId = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_channel'";

        try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(channelId)){


            preparedStatement.setString(1, UpdatingChannel.channelId);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

        }
    }
}
