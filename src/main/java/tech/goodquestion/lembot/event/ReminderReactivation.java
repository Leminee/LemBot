package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.database.QueryHelper;

import java.util.concurrent.TimeUnit;

public class ReminderReactivation extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

     Reminder.scheduleReminder(QueryHelper.getMinutesToNextBump(), TimeUnit.MINUTES);

    }
}
