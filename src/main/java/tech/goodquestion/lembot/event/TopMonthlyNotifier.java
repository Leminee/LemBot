package tech.goodquestion.lembot.event;


import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.Helper;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class TopMonthlyNotifier extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        int dayOfCurrentMonth = LocalDate.now().getDayOfMonth();
        int daysBeforeNextMonth = LocalDate.now().getMonth().maxLength() - dayOfCurrentMonth;

        Helper.scheduleCommand("topmb", daysBeforeNextMonth, 30, TimeUnit.DAYS);
        Helper.scheduleCommand("topmf", daysBeforeNextMonth, 30, TimeUnit.SECONDS);

    }
}
