package tech.goodquestion.lembot.event;


import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.Helper;

import java.util.concurrent.TimeUnit;

public class TopListReminder extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Helper.scheduleCommand("topu", 1, 48, TimeUnit.HOURS);
        Helper.scheduleCommand("topb", 1, 60, TimeUnit.HOURS);
    }

}
