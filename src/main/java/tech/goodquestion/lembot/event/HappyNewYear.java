package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HappyNewYear extends ListenerAdapter {

    public static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public void onUserUpdateOnlineStatus(final @NotNull UserUpdateOnlineStatusEvent event) {


        final boolean isSylvester = LocalDateTime.now().getMonth().getValue() == 12 && LocalDateTime.now().getDayOfMonth() == 31;

        if (!isSylvester) return;

        final LocalDateTime localDateTimeNow = LocalDateTime.now();
        final LocalDateTime localDateTimeNextYear = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        final long delay = ChronoUnit.SECONDS.between(localDateTimeNow, localDateTimeNextYear);

        final Runnable runnable = () -> {

            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {

                    System.out.println(interruptedException.getMessage());
                }
                Objects.requireNonNull(Config.getInstance().getChannel().getGeneralChannel()).sendMessage(String.valueOf(i)).queue();
            }
            Objects.requireNonNull(Config.getInstance().getChannel().getGeneralChannel()).sendMessage("https://happynewyear2021status.com/wp-content/uploads/2021/11/new-year-gifs-2022-1.gif").queue();
        };

        scheduledExecutorService.schedule(runnable, delay, TimeUnit.SECONDS);

    }
}

