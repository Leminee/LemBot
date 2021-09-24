package discord.bot.gq.event;


import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TopListReminder extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {


        Helper.sendCommand("topu", event, 168, 168, TimeUnit.HOURS);

        Helper.sendCommand("topb", event, 168, 168, TimeUnit.HOURS);

    }
}


