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

        String userMessage = event.getMessage().getContentRaw();
        String getTopFlooderCommand = "top";

        if (Helper.isValidCommand(userMessage, getTopFlooderCommand)) {

            try {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String selectTopFlooder = "SELECT username FROM user_message ORDER BY number_message DESC LIMIT 5;";
                Statement statement = db.getConnection().createStatement();
                ResultSet rS = statement.executeQuery(selectTopFlooder);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Liste der TOP 5 User");
                embedBuilder.setDescription("");
                embedBuilder.setColor(Color.white);

                int top = 1;

                while (rS.next()) {

                    embedBuilder.addField("TOP " + top, rS.getString(1).toUpperCase(), false);
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
