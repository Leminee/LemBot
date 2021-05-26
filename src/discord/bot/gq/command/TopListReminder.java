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
        String command = "sendTopCommands";

        if (!hasPermission) {
            return;
        }

        if (Helper.isValidCommand(userMessage,command)) {

            Helper.sendCommand("top", event, 36, 36, TimeUnit.HOURS);

            Helper.sendCommand("topb", event, 48, 48, TimeUnit.HOURS);
        }
    }
}

