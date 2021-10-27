package tech.goodquestion.lembot.event;


import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.Helper;

import java.util.concurrent.TimeUnit;

public class TopListReminder extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Helper.scheduleCommand("topu", 168, 168, TimeUnit.HOURS);
        Helper.scheduleCommand("topb", 168, 168, TimeUnit.HOURS);
    }

}
