package discord.bot.gq.event;


import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class CheckingTop extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {


            Helper.sendCommand("top", event, 1,36, TimeUnit.HOURS);

            Helper.sendCommand("topb", event, 1,48,TimeUnit.HOURS);
    }
}

