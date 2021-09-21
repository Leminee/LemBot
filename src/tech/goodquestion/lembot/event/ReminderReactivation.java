package tech.goodquestion.lembot.event;

import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;

public class ReminderReactivation extends ListenerAdapter {


    @Override
    public void onReady(@NotNull ReadyEvent event) {

        try {
            Helper.reactivateReminder(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
