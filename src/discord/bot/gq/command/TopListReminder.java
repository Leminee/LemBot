package discord.bot.gq.command;


import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TopListReminder extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {


        Helper.sendCommand("topu", event, 1, 48, TimeUnit.HOURS);

        Helper.sendCommand("topb", event, 1, 60, TimeUnit.HOURS);

    }
}


