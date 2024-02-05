package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class HappyNewYear extends ListenerAdapter {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final String HAPPY_NEW_YEAR_GIF_URL = "https://happynewyear2021status.com/wp-content/uploads/2021/11/new-year-gifs-2022-1.gif";

    @Override
    public void onReady(@SuppressWarnings("null") final @NotNull ReadyEvent event) {

        final LocalDateTime localDateTimeNow = LocalDateTime.now();
        final int currentYear = LocalDateTime.now().getYear();
        final LocalDateTime localDateTimeNextYear = LocalDateTime.of(currentYear, 12, 31, 23, 59, 49);
        final long delay = ChronoUnit.SECONDS.between(localDateTimeNow, localDateTimeNextYear);

        @SuppressWarnings("null")
        final Runnable runnable = () -> {

            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {

                    System.out.println(interruptedException.getMessage());
                }
                Objects.requireNonNull(Config.getInstance().getChannelConfig().getGeneralChannel()).sendMessage(String.valueOf(i)).queue();
            }


            Objects.requireNonNull(Config.getInstance().getChannelConfig().getGeneralChannel())
                    .sendMessage(HAPPY_NEW_YEAR_GIF_URL)
                    .queue();
        };

        scheduledExecutorService.schedule(runnable, delay, TimeUnit.SECONDS);
    }
}

