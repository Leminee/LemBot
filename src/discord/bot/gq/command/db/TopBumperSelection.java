package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopBumperSelection extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String topBumperCheckCommand = "topb";

        if (Helper.isValidCommand(userMessageContent, topBumperCheckCommand)) {

            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            String topBumper = "SELECT username FROM user_bump ORDER BY number_bumps DESC, username LIMIT 3;";

            try (Statement statement = connectionToDB.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(topBumper)) {

                EmbedBuilder topBumperEmbed = new EmbedBuilder();

                String embedTitle = "User mit den meisten Bumps";
                String embedDescription = "";
                String embedThumbnail = "https://cdn.discordapp.com/attachments/819694809765380146/869248076446052402/Bildschirmfoto_2021-07-26_um_17.48.08.png";
                Color embedColor = Color.cyan;

                Helper.addTopToEmbed(event, resultSet, topBumperEmbed, embedTitle, embedDescription, embedThumbnail, embedColor);

            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }
}
