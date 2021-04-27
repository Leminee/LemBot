package discord.bot.gq.db;

import discord.bot.gq.BotMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopBumper extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String commandTopBumper = "topb";

        if (isValidCommand(userMessage, commandTopBumper)) {

            try {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String selectTopBumper = "SELECT username, number_bumps FROM user_bump ORDER BY number_bumps DESC, username LIMIT 3;";
                Statement statement = db.connection.createStatement();
                ResultSet rS = statement.executeQuery(selectTopBumper);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("User mit den meisten Bumps");
                embedBuilder.setDescription("");
                embedBuilder.setColor(0x26b7b8);
                embedBuilder.setThumbnail("https://plane-dein-training.de/assets/media/dis.png");

                int top = 1;

                while (rS.next()) {

                    embedBuilder.addField("TOP " + top, rS.getString(1), false);
                    top++;

                }

                event.getChannel().sendMessage(embedBuilder.build()).queue();

                rS.close();
                statement.close();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static boolean isValidCommand(String userMessage, String command) {

        return userMessage.equalsIgnoreCase(BotMain.PREFIX + command);
    }

}
