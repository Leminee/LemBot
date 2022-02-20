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

public final class InviteLinkDeletion extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final String userMessage = event.getMessage().getContentRaw();

        if (!(userMessage.contains("https://discord.gg") || userMessage.contains("https://discord.com/invite/") || userMessage.contains("https://discord.io/"))) return;

        if (event.getChannel().getIdLong() == Config.getInstance().getChannelConfig().getNewArrivalsChannel().getIdLong()) return;

        final long channelId = event.getChannel().getIdLong();
        final Member author = event.getMember();
        assert author != null;
        final boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);

        if (isStaff || channelId == Config.getInstance().getChannelConfig().getYourProjectsChannel().getIdLong()) {
            return;
        }

        event.getMessage().delete().queue();

        final long logChannelId = Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong();

        event.getMessage().reply(":x: In diesem Kanal dürfen keine Invitelinks gepostet werden!").queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor(author.getUser().getAsTag(), null, author.getEffectiveAvatarUrl());
        embedBuilder.setTitle("EinladungsLink gelöscht");
        embedBuilder.addField("Author", author.getAsMention(), true);
        embedBuilder.addField("Author ID", String.valueOf(author.getIdLong()), true);
        embedBuilder.addField("Kanal", event.getChannel().getAsMention(), true);
        embedBuilder.addField("Grund", "```Posten eines Einladungslinks in einen nicht erlaubten Channel```", false);
        embedBuilder.addField("Nachricht", event.getMessage().getContentRaw(), false);
        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));
        embedBuilder.setTimestamp(Instant.now());

        Objects.requireNonNull(event.getGuild().getTextChannelById(logChannelId))
                .sendMessageEmbeds(embedBuilder.build())
                .queue();
    }
}
