package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.lib.Helper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Reminder extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        User embedAuthor = event.getAuthor();

        if (Helper.isNotSuccessfulBump(disBoardEmbed, embedAuthor)) return;

        scheduleReminder();
    }

    public static void scheduleReminder() {
        scheduleReminder(2, TimeUnit.HOURS);
    }

    public static void scheduleReminder(int delay, TimeUnit timeUnit) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        String bumperRole = "<@&" + Config.getInstance().getRole().getBumpRoleId() + ">";

        String[] pingContent = {
                "Jetzt kann wieder gebumpt werden " + bumperRole + " :smile: ",
                "Es ist wieder Zeit zu bumpen " + bumperRole + " :smile:",
                "Bumpe den Server jetzt! " + bumperRole + " :smile:"};

        Random random = new Random();

        final Runnable ping = () -> {
            int randomNumber = random.nextInt(pingContent.length);
            Config.getInstance().getChannel().getBumpChannel().sendMessage(pingContent[randomNumber]).queue();
        };

        scheduler.schedule(ping, delay, timeUnit);
    }
}