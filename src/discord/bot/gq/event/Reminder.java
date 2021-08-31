package discord.bot.gq.event;

import discord.bot.gq.lib.Helper;
import discord.bot.gq.config.db.ConfigSelection;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Reminder extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ConfigSelection configSelection = new ConfigSelection();
        configSelection.selectRoleId();
        configSelection.selectBotCommandsChannelId();

        String[] pingContent = {
                "Jetzt kann wieder gebumpt werden " + configSelection.getRoleId() + " :smile: ",
                "Es ist wieder Zeit zu bumpen " + configSelection.getRoleId() + " :smile:",
                "Bumpe den Server jetzt! " + configSelection.getRoleId() + " :smile:"};


        if (configSelection.getRoleId() == null || configSelection.getBotCommandsChannelId() == null) {
            return;
        }

        Random random = new Random();

        List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        User embedAuthor = event.getAuthor();


        if (Helper.isSuccessfulBump(disBoardEmbed, embedAuthor)) {

            final Runnable ping = () -> {
                int randomNumber = random.nextInt(pingContent.length);
                Objects.requireNonNull(event.getJDA().getTextChannelById(configSelection.getBotCommandsChannelId())).sendMessage(pingContent[randomNumber]).queue();
            };

            scheduler.schedule(ping, 2, TimeUnit.HOURS);

        }
    }
}