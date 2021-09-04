package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.ParseException;

public class NextBumpTime extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String nextBumpTimeCheckCommand = "nbt";
        String authorCommandAsMention = event.getAuthor().getAsMention();

        if (Helper.isValidCommand(userMessageContent, nextBumpTimeCheckCommand)) {


            try {
                event.getChannel().sendMessage("Nächster Bump um **" + String.valueOf(Helper.getNextBumpAvailabilityTime()).substring(0,5) + "** " + "Uhr " + "(in **" + (Helper.getMinutesBeforePing() + 1) + "** Minuten) " + authorCommandAsMention).queue();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        }
    }
}
