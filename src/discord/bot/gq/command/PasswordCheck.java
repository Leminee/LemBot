package discord.bot.gq.command;

import discord.bot.gq.BotMain;
import discord.bot.gq.database.ConnectionToDB;
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
        String checkCommand = "check";

        if ((userMessage[0].equalsIgnoreCase(BotMain.PREFIX + checkCommand))) {

            if (userMessage.length != 2) {
                return;
            }

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            try {

                String selectPasswords = "SELECT pass FROM leaked_password WHERE pass = ?";

                PreparedStatement pS = db.getConnection().prepareStatement(selectPasswords);
                String userPassword = userMessage[1];

                event.getMessage().delete().queue();

                pS.setString(1, userPassword);

                ResultSet rS = pS.executeQuery();

                if (rS.next()) {

                    event.getChannel().sendMessage(" :red_circle:  Pwned - Passwort wurde gefunden! " + authorCommand).queue();

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

