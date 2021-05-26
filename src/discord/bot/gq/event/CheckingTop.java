package discord.bot.gq.event;


import discord.bot.gq.config.db.ConfigSelection;
import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CheckingTop extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        boolean hasPermission = Objects.requireNonNull(event.getMessage().getMember()).hasPermission(Permission.ADMINISTRATOR);

        if (!hasPermission) {
            return;
        }

        if (userMessage.equals("sendTopCommands")) {

            ConfigSelection configSelection = new ConfigSelection();
            System.out.println(configSelection.getChannelId());

            Helper.sendCommand("top", event, 36, 36, TimeUnit.HOURS);

            Helper.sendCommand("topb", event, 48, 48, TimeUnit.HOURS);
        }
    }
}

