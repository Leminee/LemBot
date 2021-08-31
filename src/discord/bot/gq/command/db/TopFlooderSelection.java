package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import discord.bot.gq.database.ConnectionToDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopFlooderSelection extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String topFlooderCheckCommand = "topu";

        if (Helper.isValidCommand(userMessageContent, topFlooderCheckCommand)) {

            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            String topFlooder = "SELECT username FROM user_message ORDER BY number_message DESC LIMIT 3;";

            try (Statement statement = connectionToDB.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(topFlooder)) {

                EmbedBuilder topFlooderEmbed = new EmbedBuilder();

                String embedTitle = "User mit den meisten Nachrichten";
                String embedDescription = "";
                String embedThumbnail = "https://cdn.discordapp.com/attachments/819694809765380146/844312789531230208/typing.png";
                Color embedColor = Color.white;

                Helper.addTopToEmbed(event, resultSet, topFlooderEmbed, embedTitle, embedDescription, embedThumbnail, embedColor);


            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }
}
