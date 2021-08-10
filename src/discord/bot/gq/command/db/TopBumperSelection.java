package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import discord.bot.gq.database.ConnectionToDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopBumperSelection extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String topBumperCommand = "topb";


        if (Helper.isValidCommand(userMessage, topBumperCommand)) {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();
            String topBumper = "SELECT username FROM user_bump ORDER BY number_bumps DESC, username LIMIT 3;";

            try (Statement statement = db.getConnection().createStatement(); ResultSet rS = statement.executeQuery(topBumper)) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("User mit den meisten Bumps");
                embedBuilder.setDescription("");
                embedBuilder.setColor(0x26b7b8);
                embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/819694809765380146/869248076446052402/Bildschirmfoto_2021-07-26_um_17.48.08.png");

              //  Helper.createEmbed("User mit den meisten Bumps", "", "0x26b7b8","https://cdn.discordapp.com/attachments/819694809765380146/869248076446052402/Bildschirmfoto_2021-07-26_um_17.48.08.png");

                int top = 1;

                while (rS.next()) {

                    embedBuilder.addField("TOP " + top, rS.getString(1), false);
                    top++;

                }
                event.getChannel().sendMessage(embedBuilder.build()).queue();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
