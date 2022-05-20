package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class SpamDetection extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final long userId = event.getMessage().getAuthor().getIdLong();
        final boolean senderIsBot = event.getMessage().getAuthor().isBot();
        final boolean senderIsStaff = Objects.requireNonNull(event.getMessage().getMember()).hasPermission(Permission.MESSAGE_MANAGE);
        final Member member = event.getMember();
        final String messageContent = event.getMessage().getContentRaw();
        assert member != null;
        final Member bot = event.getGuild().retrieveMemberById(Config.getInstance().getBotConfig().getId()).complete();

        final Sanction sanction = new Sanction();
        sanction.userId = userId;
        sanction.author = bot.getUser().getAsTag();
        sanction.userTag = event.getMessage().getAuthor().getAsTag();
        sanction.userName = event.getMessage().getAuthor().getName();
        sanction.reason = "Verdacht auf Scam";
        sanction.channelName = event.getMessage().getChannel().getName();
        sanction.reason = "5 Hours";

        if (senderIsBot || senderIsStaff || messageContent.length() < 10) return;

        if (QueryHelper.isSpammer(userId, messageContent)) {

            timeOutScammer(member, event);

            QueryHelper.deleteScammerMessages(event, userId, messageContent);

            CommandHelper.logMemberMute(sanction);

            event.getMessage().reply(":mute: Timeout aufgrund Verdacht auf Scam durch den Bot").queue();
        }
    }

    private void timeOutScammer(final Member member, MessageReceivedEvent event) {

        final int durationInHours = 5;
        member.timeoutFor(Duration.ofHours(durationInHours)).queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl());
        embedBuilder.setTitle("Verdacht auf Scam");
        embedBuilder.setDescription("Timeout aufgrund von Verdacht auf Scam");
        embedBuilder.addField("Member", member.getAsMention(), true);
        embedBuilder.addField("Dauer", durationInHours + " Stunden", true);
        embedBuilder.addField("Grund", "```Verdacht auf Scam```", true);
        embedBuilder.addField("Nachricht", event.getMessage().getContentRaw(), false);
        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));
        embedBuilder.setTimestamp(Instant.now());

        Objects.requireNonNull(Config.getInstance()
                .getGuild()
                .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                .sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
