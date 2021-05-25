package discord.bot.gq.command;

import discord.bot.gq.database.ConnectionToDB;
import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class NumberMessagesCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String numberMessagesCheckCommand = "hmm";
        String authorCommand = event.getAuthor().getAsMention();
        long authorId = event.getAuthor().getIdLong();

        if (Helper.isValidCommand(userMessage, numberMessagesCheckCommand)) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                try {

                    String numberMessagesCheck = "SELECT number_message FROM user_message WHERE id_discord = ?";
                    PreparedStatement pS = db.getConnection().prepareStatement(numberMessagesCheck);
                    pS.setLong(1, authorId);
                    ResultSet rS = pS.executeQuery();


                    if (rS.next()) {

                        int numberMessages = rS.getInt(1);

                        String nextUser = "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1";
                        PreparedStatement pSTwo = db.getConnection().prepareStatement(nextUser);
                        pSTwo.setLong(1, numberMessages);
                        ResultSet rSTwo = pSTwo.executeQuery();

                        if (rSTwo.next()) {

                            long nextTopUserId = rSTwo.getLong(1);
                            String mentionedUser = event.getJDA().retrieveUserById(nextTopUserId).complete().getAsMention();
                            int nextTopUserNumberMessage = rSTwo.getInt(2);

                            EmbedBuilder numberMessagesInfo = new EmbedBuilder();
                            numberMessagesInfo.setColor(0x0E0E42);
                            numberMessagesInfo.setTitle("Information");
                            numberMessagesInfo.setDescription("Anzahl deiner Nachrichten: " + "**" + numberMessages + "**" + " " + authorCommand + "\n" + "Du bist hinter dem User " + mentionedUser + " **(" + nextTopUserNumberMessage + " Nachrichten)**"  + ":yum: ");
                            event.getChannel().sendMessage(numberMessagesInfo.build()).queue();
                        } else {
                            event.getChannel().sendMessage(" :first_place: Du bist **TOP 1** mit " + numberMessages + " Nachrichten " + authorCommand).queue();
                        }

                    }
                    pS.close();
                    rS.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }


    }
}
