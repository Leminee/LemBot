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

public class NumberBumpsCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String numberBumpsCheckCommand = "hmb";
        String authorCommand = event.getAuthor().getAsMention();
        long authorId = event.getAuthor().getIdLong();

        if (Helper.isValidCommand(userMessage, numberBumpsCheckCommand)) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                try {

                    String numberBumpsCheck = "SELECT number_bumps FROM user_bump WHERE id_discord = ?";
                    PreparedStatement pS = db.getConnection().prepareStatement(numberBumpsCheck);
                    pS.setLong(1, authorId);
                    ResultSet rS = pS.executeQuery();


                    if (rS.next()) {

                        int numberBumps = rS.getInt(1);

                        String nextUser = "SELECT id_discord, number_bumps FROM user_bump WHERE number_bumps > ? ORDER BY number_bumps, username LIMIT 1";
                        PreparedStatement pSTwo = db.getConnection().prepareStatement(nextUser);
                        pSTwo.setLong(1, numberBumps);
                        ResultSet rSTwo = pSTwo.executeQuery();

                        if (rSTwo.next()) {

                            long nextTopUserId = rSTwo.getLong(1);
                            String mentionedUser = event.getJDA().retrieveUserById(nextTopUserId).complete().getAsMention();
                            int nextTopUserNumberBump = rSTwo.getInt(2);

                            EmbedBuilder numberBumpsInfo = new EmbedBuilder();
                            numberBumpsInfo.setColor(0x26b7b8);
                            numberBumpsInfo.setTitle("Information");
                            numberBumpsInfo.setDescription("Anzahl deiner Bumps: " + "**" + numberBumps + "**" + " " + authorCommand + "\n" + "Du bist hinter dem User " + mentionedUser + " **(" + nextTopUserNumberBump + " Bumps)**");
                            event.getChannel().sendMessage(numberBumpsInfo.build()).queue();
                        } else {
                            event.getChannel().sendMessage(" :first_place: Du bist **TOP 1** mit " + numberBumps+ " Bumps " + authorCommand).queue();
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
