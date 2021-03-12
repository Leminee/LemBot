package discord.bot.gq;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RemindMe extends ListenerAdapter {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String roleId = "<@&815922232106156033>";
        String[] pingContent = {
                "Jetzt kann wieder gebumpt werden " + roleId + " :smile: ",
                "Es ist wieder Zeit zu bumpen " + roleId + " :smile:",
                "Bumpe den Server jetzt! " + roleId + " :smile:",
                "Jetzt geht es wieder los " + roleId + " :smile:"};

        Random random = new Random();

        List <MessageEmbed> messages = event.getMessage().getEmbeds();
        User author = event.getAuthor();

        if (!author.getId().equals("302050872383242240")) {
            return;
        }
        if (messages.isEmpty()) {
            return;
        }
        if (messages.get(0).getDescription() == null) {
            return;
        }
        if (!Objects.requireNonNull(messages.get(0).getDescription()).contains("https://disboard.org/")) {
            return;
        }

        final Runnable ping = () -> { int randomNumber = random.nextInt(pingContent.length);
        event.getChannel().sendMessage(pingContent[randomNumber]).queue();};

        scheduler.schedule(ping, 2, TimeUnit.HOURS);

        //scheduler.scheduleWithFixedDelay(ping,5, 5, TimeUnit.SECONDS);


    }

}
















