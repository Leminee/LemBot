package tech.goodquestion.lembot.event;


import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.Helper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TopMonthlyNotifier extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        int dayOfCurrentMonth = LocalDate.now().getDayOfMonth();
        int daysBeforeNextMonth = LocalDate.now().getMonth().maxLength() - dayOfCurrentMonth;

        LocalDate firstDayThisMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate firstDayNextMonth = firstDayThisMonth.plusMonths(1);
        long period = ChronoUnit.DAYS.between(firstDayThisMonth, firstDayNextMonth);

        Helper.scheduleCommand("topmb", daysBeforeNextMonth, period, TimeUnit.DAYS);
        Helper.scheduleCommand("topmf", daysBeforeNextMonth, period, TimeUnit.DAYS);
    }
}
