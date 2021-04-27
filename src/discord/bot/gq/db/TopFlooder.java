package discord.bot.gq.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopFlooder extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String commandTopUser ="top";

        if (TopBumper.handleCommand(userMessage,commandTopUser)) {

            try {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String selectTopUser = "SELECT username, number_message FROM user_message ORDER BY number_message DESC LIMIT 3;";
                Statement statement = db.connection.createStatement();
                ResultSet rS = statement.executeQuery(selectTopUser);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Liste der TOP 3 User");
                embedBuilder.setDescription("");
                embedBuilder.setColor(Color.white);

                int top = 1;

                while (rS.next()) {

                    embedBuilder.addField("TOP " + top, rS.getString(1).toUpperCase(), false);
                    embedBuilder.addField("Anzahl Nachrichten", rS.getString(2), false);
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
}
