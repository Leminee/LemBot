package discord.bot.gq.command;

import discord.bot.gq.database.ConnectionToDB;
import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] userMessageContent = event.getMessage().getContentRaw().split("\\s+");
        String authorCommand = event.getAuthor().getAsMention();
        String passwordCheckCommand = "check";

        if ((userMessageContent[0].equalsIgnoreCase(Helper.PREFIX + passwordCheckCommand))) {

            if (userMessageContent.length != 2) {
                return;
            }

            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            String passwordCheck = "SELECT pass FROM leaked_password WHERE pass = ?";
            String userPassword = userMessageContent[1];

            try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(passwordCheck)) {


                preparedStatement.setString(1, userPassword);

                ResultSet resultSet = preparedStatement.executeQuery();

                event.getMessage().delete().queue();


                if (resultSet.next()) {

                    event.getChannel().sendMessage(" :red_circle:  Pwned - Passwort wurde gefunden! " + authorCommand).queue();


                } else {
                    event.getChannel().sendMessage(" :green_circle:  Nicht gefunden! " + authorCommand).queue();

                }


            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }
}

