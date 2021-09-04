package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class NextBumpTime extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String nextBumpTimeCheckCommand = "nbt";
        long authorCommandId = event.getAuthor().getIdLong();

        if (Helper.isValidCommand(userMessageContent, nextBumpTimeCheckCommand)) {

            int latestBumpHour = Integer.parseInt(Helper.getLatestBumpTime().substring(0, 2));

            int nextBumpHour = latestBumpHour + 1;

            String nextBumpTime = Helper.getLatestBumpTime().replace(Helper.getLatestBumpTime().substring(0, 2), String.valueOf(nextBumpHour));

            String authorCommandAsMention = Objects.requireNonNull(event.getGuild().getMemberById(authorCommandId)).getAsMention();

            if (Helper.getLatestBumpTime().startsWith("0")) {

                event.getChannel().sendMessage("Nächster Bump um **0" + nextBumpTime + "** " + "Uhr " + authorCommandAsMention).queue();
            } else {

                event.getChannel().sendMessage("Nächster Bump um **" + nextBumpTime + "** " + "Uhr " + authorCommandAsMention).queue();
            }
        }
    }
}
