package tech.goodquestion.lembot.event;


import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.Helper;

import java.util.concurrent.TimeUnit;

public class TopListReminder extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
<<<<<<< HEAD:src/main/java/tech/goodquestion/lembot/event/TopListReminder.java
        Helper.scheduleCommand("topu", 1, 48, TimeUnit.HOURS);
        Helper.scheduleCommand("topb", 1, 60, TimeUnit.HOURS);
=======


        Helper.sendCommand("topu", event, 168, 168, TimeUnit.HOURS);

        Helper.sendCommand("topb", event, 168, 168, TimeUnit.HOURS);

>>>>>>> 694a48544f3018afe52a829e2638539aac17f7d4:src/discord/bot/gq/event/TopListReminder.java
    }

}
