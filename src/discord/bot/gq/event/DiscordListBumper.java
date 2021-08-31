package discord.bot.gq.event;

import discord.bot.gq.config.db.ConfigSelection;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordListBumper extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        String bumpCommand = "dlm!bump";

        ConfigSelection configSelection = new ConfigSelection();
        configSelection.selectBotCommandsChannelId();

        if (configSelection.getBotCommandsChannelId() == null) {
            return;
        }

        List<MessageEmbed> discordListBot = event.getMessage().getEmbeds();
        User embedAuthor = event.getAuthor();

        if (isSuccessfulBumpDiscordList(discordListBot, embedAuthor)) {

            final Runnable ping = () -> Objects.requireNonNull(event.getJDA().getTextChannelById(configSelection.getBotCommandsChannelId())).sendMessage(bumpCommand).queue();

            scheduler.schedule(ping, 9, TimeUnit.HOURS);

        }

    }

    private boolean isSuccessfulBumpDiscordList(List<MessageEmbed> embedContent, User embedAuthor) {

        long discordListBot = 212681528730189824L;

        if (embedAuthor.getIdLong() != discordListBot) {
            return false;
        }

        if (embedContent.get(0).getDescription() == null) {
            return false;
        }
        return Objects.requireNonNull(embedContent.get(0).getDescription()).contains("Server bumped!");
    }
}
