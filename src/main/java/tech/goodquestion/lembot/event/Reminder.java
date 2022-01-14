package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.lib.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Reminder extends ListenerAdapter {

    private static final List<ScheduledFuture<?>> tasks = new ArrayList<>();

    @Override
    public void onGuildMessageReceived(@NotNull final GuildMessageReceivedEvent event) {

        final List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        final User embedAuthor = event.getAuthor();

        if (Helper.isNotSuccessfulBump(disBoardEmbed,embedAuthor)) return;

        scheduleReminder();

        if (tasks.size() > 1) {
            for (int i = 1; i < tasks.size(); i++) {
                tasks.get(i).cancel(false);
            }
        }
    }

    private void scheduleReminder() {
        final int reminderDelay = 2;
        scheduleReminder(reminderDelay, TimeUnit.HOURS);
    }

    public static void scheduleReminder(final int delay, final TimeUnit timeUnit) {

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final String bumperRoleAsMention = "<@&" + Config.getInstance().getRole().getBumpRoleId() + ">";

        final String[] pingContent = {
                "Jetzt kann wieder gebumpt werden " + bumperRoleAsMention + " :grin:",
                "Es ist wieder Zeit zu bumpen " + bumperRoleAsMention + " :timer:",
                "Bumpe den Server jetzt " + bumperRoleAsMention + " :grinning:"
        };

        final Random random = new Random();

        final Runnable ping = () -> {
            int randomNumber = random.nextInt(pingContent.length);
            Config.getInstance().getChannel().getBumpChannel().sendMessage(pingContent[randomNumber]).queue();
            tasks.clear();
        };

        ScheduledFuture<?> scheduledFuture = scheduler.schedule(ping, delay, timeUnit);
        tasks.add(scheduledFuture);

    }
}