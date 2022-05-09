package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class BumpReminder extends ListenerAdapter {

    private final List<ScheduledFuture<?>> tasks = new ArrayList<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {

        final List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        final User embedAuthor = event.getAuthor();

        if (Helper.isNotSuccessfulBump(disBoardEmbed, embedAuthor)) return;

        scheduleReminder();

        final boolean isRedundantTask = tasks.size() > 1;

        if (isRedundantTask) cancelRedundantTask();

    }

    private void scheduleReminder() {
        final int reminderDelay = 2;
        scheduleReminder(reminderDelay, TimeUnit.HOURS);
    }

    public void scheduleReminder(final int reminderDelay, final TimeUnit timeUnit) {

        final String bumperRoleAsMention = "<@&" + Config.getInstance().getRoleConfig().getBumpRoleId() + ">";

        final String[] pingContent = {
                "Jetzt kann wieder gebumpt werden " + bumperRoleAsMention + " :grin:",
                "Es ist wieder Zeit zu bumpen " + bumperRoleAsMention + " :timer:",
                "Bumpe den Server jetzt " + bumperRoleAsMention + " :grinning:"
        };

        final Runnable runnable = () -> {
            final int randomNumber = ThreadLocalRandom.current().nextInt(pingContent.length);
            Config.getInstance().getChannelConfig().getBumpChannel().sendMessage(pingContent[randomNumber]).queue();
            tasks.clear();
        };

        final ScheduledFuture<?> task = scheduledExecutorService.schedule(runnable, reminderDelay, timeUnit);
        tasks.add(task);
    }

    private void cancelRedundantTask() {

        final List<ScheduledFuture<?>> runningTasks = tasks
                .stream()
                .filter(task -> !task.isCancelled()).toList();

        runningTasks.get(1).cancel(false);
    }
}