package discord.bot.gq.command.db;

import discord.bot.gq.database.ConnectionToDB;
import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class ActiveUserRecord extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String userNameAsMention = Objects.requireNonNull(event.getMember()).getAsMention();
        String recordCheckCommand = "aur";
        int currentRecord;


        if (Helper.isValidCommand(userMessageContent, recordCheckCommand)) {

            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            String activeUserRecord = "SELECT MAX(active_member) FROM active_user;";

            try (Statement statement = connectionToDB.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(activeUserRecord)) {

                if (resultSet.next()) {

                    if (!event.getMember().getUser().isBot()) {
                        currentRecord = resultSet.getInt(1);

                        event.getChannel().sendMessage("Maximale Anzahl der gleichzeitig aktiven User auf dem Server: " + "**" + currentRecord + "**" + " " + userNameAsMention).queue();
                    }
                }

            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }
}
