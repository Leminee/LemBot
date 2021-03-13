package discord.bot.gq.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDB {
    public Connection connection;

    public void initialize() {
        try {

            String dbUrl = "jdbc:mysql://localhost:3306/discordbot?autoReconnect=true&serverTimezone=UTC";
            String dbUsername = "root";
            String dbPassword = "admin";
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        }
    }
}
