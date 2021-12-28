package tech.goodquestion.lembot.event;


import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.Helper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TopMonthlyNotifier extends ListenerAdapter {

    @Override
    public void onReady(@NotNull final ReadyEvent event) {

        final int dayOfCurrentMonth = LocalDateTime.now().getDayOfMonth();
        final int daysBeforeNextMonth = LocalDateTime.now().getMonth().maxLength() - dayOfCurrentMonth;
        final long daysBeforeNextMonthInSeconds = daysBeforeNextMonth * 86400;

        final LocalDateTime firstDayThisMonth = LocalDateTime.now();
        final LocalDateTime firstDayNextMonth = firstDayThisMonth.plusMonths(1);
        final long period = ChronoUnit.SECONDS.between(firstDayThisMonth, firstDayNextMonth);

        Helper.scheduleCommand("topmb", daysBeforeNextMonthInSeconds, period, TimeUnit.SECONDS);
        Helper.scheduleCommand("topmf", daysBeforeNextMonthInSeconds, period, TimeUnit.SECONDS);
    }
}
