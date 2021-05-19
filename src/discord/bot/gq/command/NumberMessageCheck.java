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

public class NumberMessageCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String numberMessageCheckCommand = "hmm";
        String authorCommand = event.getAuthor().getAsMention();
        long userId = event.getAuthor().getIdLong();

        if (Helper.isValidCommand(userMessage, numberMessageCheckCommand)) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {


                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                try {

                    String numberMessageCheck = "SELECT number_message FROM user_message WHERE id_discord = ?";
                    PreparedStatement pS = db.getConnection().prepareStatement(numberMessageCheck);
                    pS.setLong(1, userId);
                    ResultSet rS = pS.executeQuery();


                    if (rS.next()) {

                        int numberMessage = rS.getInt(1);

                        String nextUser = "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1";
                        PreparedStatement pSTwo = db.getConnection().prepareStatement(nextUser);
                        pSTwo.setLong(1, numberMessage);
                        ResultSet rSTwo = pSTwo.executeQuery();

                        if (rSTwo.next()) {

                            long nextUserId = rSTwo.getLong(1);
                            String mentionedUser = event.getJDA().retrieveUserById(nextUserId).complete().getAsMention();
                            int nextUserNumberMessage = rSTwo.getInt(2);

                            EmbedBuilder numberMessageInfo = new EmbedBuilder();
                            numberMessageInfo.setColor(0x0E0E42);
                            numberMessageInfo.setTitle("Information");
                            numberMessageInfo.setDescription("Anzahl deiner Nachrichten: " + "**" +numberMessage + "**" +  " " + authorCommand + "\n" + "Du bist hinter dem User " + mentionedUser + " (**" + nextUserNumberMessage + " Nachrichten)**");
                            event.getChannel().sendMessage(numberMessageInfo.build()).queue();
                        }
                        else {
                            event.getChannel().sendMessage(" :first_place: Du bist **TOP 1** mit " + numberMessage + " Nachrichten " + authorCommand).queue();
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
