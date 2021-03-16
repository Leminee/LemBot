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

public class Reminder extends ListenerAdapter {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String roleId = "<@&815922232106156033>";
        String[] pingContent = {
                "Jetzt kann wieder gebumpt werden " + roleId + " :smile: ",
                "Es ist wieder Zeit zu bumpen " + roleId + " :smile:",
                "Bumpe den Server jetzt! " + roleId + " :smile:"};

        Random random = new Random();

        List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        User author = event.getAuthor();

        if (isSuccessBump(disBoardEmbed, author)) {

            final Runnable ping = () -> {
                int randomNumber = random.nextInt(pingContent.length);
                Objects.requireNonNull(event.getJDA().getTextChannelById("784907135900581928")).sendMessage(pingContent[randomNumber]).queue();
            };

            scheduler.schedule(ping, 2, TimeUnit.HOURS);
        }
    }
    public static boolean isSuccessBump(List<MessageEmbed> messages, User author) {

        if (!author.getId().equals("302050872383242240")) {
            return false;
        }
        if (messages.isEmpty()) {
            return false;
        }
        if (messages.get(0).getDescription() == null) {
            return false;
        }

        if(messages.get(0).getImage() == null) {
            return false;
        }
        return Objects.equals(Objects.requireNonNull(messages.get(0).getImage()).getUrl(), "https://disboard.org/images/bot-command-image-bump.png");
    }

}
