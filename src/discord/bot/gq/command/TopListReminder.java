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

        String userMessageContent = event.getMessage().getContentRaw();
        boolean isAdmin = Objects.requireNonNull(event.getMessage().getMember()).hasPermission(Permission.ADMINISTRATOR);
        String author = event.getAuthor().getAsMention();
        String remindTopListCommand = "stc";

        if (!isAdmin) {
            return;
        }

        if (Helper.isValidCommand(userMessageContent, remindTopListCommand)) {

            event.getChannel().sendMessage("TopListReminder erfolgreich aktiviert " + author + "!").queue();

            Helper.sendCommand("topu", event, 1, 36, TimeUnit.HOURS);

            Helper.sendCommand("topb", event, 1, 48, TimeUnit.HOURS);

        }
    }
}

