package discord.bot.gq.database.config;

import discord.bot.gq.database.ConnectionToDB;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfigUpdating {

    public void updateRoleId() {

        try {
            ConnectionToDB db = new ConnectionToDB();
            db.initialize();
            String updateQuery = "UPDATE config SET config_value = ? WHERE config_name = 'id_ping_role'";
            PreparedStatement updatePStatement = db.connection.prepareStatement(updateQuery);
            updatePStatement.setString(1, "");
            //updatePStatement.executeUpdate();

            //TODO

        } catch (SQLException e) {
            e.getMessage();

        }
    }
}
