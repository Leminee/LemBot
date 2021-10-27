package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.BotCommand;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopMessageCommand implements BotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        Connection conn = DatabaseConnector.openConnection();
        String topFlooder = "SELECT username FROM user_message ORDER BY number_message DESC LIMIT 3;";

        try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(topFlooder)) {
            EmbedBuilder topFlooderEmbed = new EmbedBuilder();

            String embedTitle = "User mit den meisten Nachrichten";
            String embedDescription = "";
            String embedThumbnail = "https://cdn.discordapp.com/attachments/819694809765380146/844312789531230208/typing.png";
            Color embedColor = Color.white;

            Helper.addTopToEmbed(resultSet, topFlooderEmbed, embedTitle, embedDescription, embedThumbnail, embedColor, channel);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public String getName() {
        return "topu";
    }

    @Override
    public String getDescription() {
        return "`?topu`: Zeigt die User mit den meisten Nachrichten an";
    }
}
