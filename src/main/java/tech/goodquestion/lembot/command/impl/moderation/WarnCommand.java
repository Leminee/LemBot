package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class WarnCommand extends UserBanishment {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    @Override
    public void banishUser(final User toBanish, final Sanction sanction, final Message originMessage) {

        originMessage.getGuild().addRoleToMember(sanction.userId, Config.getInstance().getRoleConfig().getWarnRole()).queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setAuthor(toBanish.getAsTag(), null,toBanish.getEffectiveAvatarUrl());
        embedBuilder.setDescription("**" + ":warning: Member verwarnt" + "**");
        embedBuilder.addField("Verwarnter Member", toBanish.getAsMention(), true);
        embedBuilder.addField("Verwarnt von",originMessage.getMember().getAsMention(), true);
        embedBuilder.addField("Grund",  "```" + sanction.reason + "```", false);
        embedBuilder.setFooter(originMessage.getMember().getUser().getAsTag(),originMessage.getMember().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        Helper.sendEmbed(embedBuilder,originMessage,false);

        final String performedSanction = SanctionType.WARN.getVerbalizedSanctionTyp();
        final SanctionType sanctionType = SanctionType.WARN;

        notifyWarnedUser(toBanish, sanctionType, performedSanction, sanction.reason, Objects.requireNonNull(originMessage.getMember()));

        CommandHelper.logUserWarn(sanction);

    }

    private void notifyWarnedUser(final User sanctionedUser, final SanctionType sanctionType, final String performedSanction, final String reason, final Member sanctionAuthor){

        try {

            final  EmbedBuilder embedBuilder = new EmbedBuilder();
            final String defaultWarnDuration = "28 Tage";

            Helper.createEmbed(embedBuilder, ":warning: " + sanctionType, "Du wurdest auf **GoodQuestion** " + " **" + performedSanction + "**", EmbedColorHelper.WARN);
            embedBuilder.addField("Dauer", defaultWarnDuration, true);
            embedBuilder.addField("Verwarnt von", sanctionAuthor.getAsMention(), true);
            embedBuilder.addField("Grund",  "```" + reason + "```", false);
            embedBuilder.setFooter(sanctionAuthor.getUser().getAsTag(),sanctionAuthor.getUser().getEffectiveAvatarUrl());
            embedBuilder.setTimestamp(Instant.now());

            sanctionedUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                    .complete();

            scheduleReminder(28, TimeUnit.DAYS,sanctionAuthor);

        } catch (ErrorResponseException errorResponseException) {

            Objects.requireNonNull(Config.getInstance().getGuild()
                    .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                    .sendMessage(errorResponseException.getMessage() + " " +  sanctionedUser.getAsTag()).queue();

            System.out.println(errorResponseException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(errorResponseException, this.getClass().getName()));
        }
    }

    public void scheduleReminder(final long delay, final TimeUnit timeUnit, Member sanctionedMember) {


        final Runnable runnable = () -> notifyStaff(sanctionedMember);

        scheduledExecutorService.schedule(runnable, delay, timeUnit);

    }

    private void notifyStaff(Member sanctionedMember){

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));

        embedBuilder.setTitle(" :mute: Verwarnung entfernt");
        embedBuilder.setAuthor(sanctionedMember.getUser().getAsTag(), null,sanctionedMember.getUser().getEffectiveAvatarUrl());
        embedBuilder.addField("Member", sanctionedMember.getAsMention(), true);
        embedBuilder.addField("Grund",  "```Ende der Mutedauer```", false);
        embedBuilder.setTimestamp(Instant.now());

        Config.getInstance().getChannelConfig().getAutoModerationChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public boolean requiresAdmin() {
        return true;
    }

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "`warn <user> <reason>`: Weist " + Config.getInstance().getRoleConfig().getWarnRole().getAsMention() + " zu";
    }
}
