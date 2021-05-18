package discord.bot.gq.command;

import discord.bot.gq.database.ConnectionToDB;
import discord.bot.gq.lib.Helper;
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

                        event.getChannel().sendMessage(" Anzahl deiner Nachrichten: " + numberMessage + " " + authorCommand).queue();

                    } else {
                        event.getChannel().sendMessage(" :red_circle: Ein unerwarteter Fehler ist aufgetreten! <@739143338975952959>").queue();

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
