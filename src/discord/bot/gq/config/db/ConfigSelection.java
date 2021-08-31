package discord.bot.gq.config.db;

import discord.bot.gq.database.ConnectionToDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfigSelection {
    private String roleId = null;
    private String botCommandsChannelId = null;
    private String newArrivalsChannelId = null;


    public void selectRoleId() {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();
        String roleId = "SELECT config_value FROM config WHERE config_name = 'id_ping_role'";

        try(Statement statement = connectionToDB.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(roleId);) {

            if (resultSet.next()) {
                this.roleId = resultSet.getString(1);

            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

        }
    }

    public void selectBotCommandsChannelId() {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String channelId = "SELECT config_value FROM config WHERE config_name = 'id_ping_channel'";

        try(Statement statement = connectionToDB.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(channelId);) {


            if (resultSet.next()) {
                this.botCommandsChannelId = resultSet.getString(1);
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

    }

    public void selectNewArrivalsChannelId() {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String channelId = "SELECT config_value FROM config WHERE config_name = 'id_welcome_channel'";

        try(Statement statement = connectionToDB.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(channelId);) {


            if (resultSet.next()) {
                this.newArrivalsChannelId = resultSet.getString(1);
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

    }

    public String getRoleId() {
        return roleId;
    }

    public String getBotCommandsChannelId() {
        return botCommandsChannelId;
    }

    public String getNewArrivalsChannelId() {
        return newArrivalsChannelId;
    }
}