package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

public final class LinkDeletion extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final String userMessage = event.getMessage().getContentRaw();

        if (!userMessage.contains("https://") && !userMessage.contains("http://")) return;

        long channelId = event.getChannel().getIdLong();
        Member author = event.getMember();
        assert author != null;
        boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);

        if (channelId != Config.getInstance().getChannelConfig().getNewArrivalsChannel().getIdLong() || isStaff) {
            return;
        }

        event.getMessage().delete().queue();

        event.getMessage().reply(":x: Nachricht wurde gelöscht, da sie einen Link enthält, der nicht verifiziert werden konnte").queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor(author.getUser().getAsTag(), null, author.getEffectiveAvatarUrl());
        embedBuilder.setTitle("Link gelöscht");
        embedBuilder.addField("Author", author.getAsMention(), true);
        embedBuilder.addField("Channel", event.getChannel().getAsMention(), true);
        embedBuilder.addField("Grund", "```Nicht verifizierter Link```", false);
        embedBuilder.addField("Nachricht", event.getMessage().getContentRaw(), false);
        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));
        embedBuilder.setTimestamp(Instant.now());

        Objects.requireNonNull(event.getGuild().getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                .sendMessageEmbeds(embedBuilder.build())
                .queue();
    }
}
