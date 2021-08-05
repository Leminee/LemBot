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
        public void onGuildMessageReceived (@NotNull GuildMessageReceivedEvent event){

            String userMessage = event.getMessage().getContentRaw();
            String userName = Objects.requireNonNull(event.getMember()).getAsMention();
            int maxUser;


            if (userMessage.equalsIgnoreCase(Helper.PREFIX + "aur")) {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String activeRecord = "SELECT MAX(active_member) FROM `active_user`";

                try(Statement statement = db.getConnection().createStatement(); ResultSet rS = statement.executeQuery(activeRecord)) {

                    if (rS.next()) {

                        if (!event.getMember().getUser().isBot()) {
                            maxUser = rS.getInt(1);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessage("Maximale Anzahl aktiven User " + "**" +maxUser + "**" + " " +  userName).queue();
                        }
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
}
