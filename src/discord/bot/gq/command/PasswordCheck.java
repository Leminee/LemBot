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

        String[] userMessage = event.getMessage().getContentRaw().split("\\s+");
        String authorCommand = event.getAuthor().getAsMention();
        String passwordCheckCommand = "check";

        if ((userMessage[0].equalsIgnoreCase(Helper.PREFIX + passwordCheckCommand))) {

            if (userMessage.length != 2) {
                return;
            }

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            try {

                String passwordCheck = "SELECT pass FROM leaked_password WHERE pass = ?";

                PreparedStatement pS = db.getConnection().prepareStatement(passwordCheck);
                String userPassword = userMessage[1];

                event.getMessage().delete().queue();

                pS.setString(1, userPassword);

                ResultSet rS = pS.executeQuery();

                if (rS.next()) {

                    event.getChannel().sendMessage(" :red_circle:  Pwned - Passwort wurde gefunden! " + authorCommand).queue();
                    long lastMessageId = event.getChannel().getLatestMessageIdLong();
                    event.getChannel().deleteMessageById(lastMessageId);

                } else {
                    event.getChannel().sendMessage(" :green_circle:  Nicht gefunden! " + authorCommand).queue();

                }
                pS.close();
                rS.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}

