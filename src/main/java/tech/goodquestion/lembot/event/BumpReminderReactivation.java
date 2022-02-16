package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.database.QueryHelper;

import java.util.concurrent.TimeUnit;

public final class BumpReminderReactivation extends ListenerAdapter {

    private final BumpReminder bumpReminder;

    public BumpReminderReactivation(BumpReminder bumpReminder) {
        this.bumpReminder = bumpReminder;
    }

    @Override
    public void onReady(@NotNull final ReadyEvent event) {

        bumpReminder.scheduleReminder(QueryHelper.getMinutesToNextBump() + 1, TimeUnit.MINUTES);
    }
}
