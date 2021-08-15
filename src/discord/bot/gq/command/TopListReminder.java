package discord.bot.gq.command;


import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TopListReminder extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        boolean hasPermission = Objects.requireNonNull(event.getMessage().getMember()).hasPermission(Permission.ADMINISTRATOR);
        String author = event.getAuthor().getAsMention();
        String command = "stc";

        if (!hasPermission) {
            return;
        }

        if (Helper.isValidCommand(userMessage, command)) {

            event.getChannel().sendMessage("Done " + author + "!").queue();

            Helper.sendCommand("topu", event, 1, 36, TimeUnit.HOURS);

            Helper.sendCommand("topb", event, 1, 48, TimeUnit.HOURS);

        }
    }
}

