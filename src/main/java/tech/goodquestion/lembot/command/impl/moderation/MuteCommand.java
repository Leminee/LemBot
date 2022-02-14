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
import java.util.concurrent.*;

public final class MuteCommand extends UserBanishCommand {


    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public void banishUser(final Member toBanish, final Sanction sanction, final Message originMessage){

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        final String performedSanction = SanctionType.MUTE.getVerbalizedSanctionTyp();
        final SanctionType sanctionType = SanctionType.MUTE;

        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));

        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setDescription("**" + ":mute: Member gemutet" + "**");
        embedBuilder.setAuthor(toBanish.getUser().getAsTag(), null,toBanish.getUser().getEffectiveAvatarUrl());
        embedBuilder.addField("Gemuteter Member", toBanish.getAsMention(), true);
        embedBuilder.addField("Gemutet von", Objects.requireNonNull(originMessage.getMember()).getAsMention(), true);
        embedBuilder.addField("Dauer", String.valueOf(sanction.duration), true);
        embedBuilder.addField("Grund",  "```" + sanction.reason + "```", false);
        embedBuilder.setFooter(originMessage.getMember().getUser().getAsTag(),originMessage.getMember().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        toBanish.timeoutFor(sanction.duration, TimeUnit.MINUTES).queue();

        Helper.sendEmbed(embedBuilder,originMessage,false);

        notifyMutedUser(toBanish.getUser(),sanctionType,performedSanction,sanction.reason,sanction.duration,originMessage.getMember());

        CommandHelper.logUserMute(sanction);

        scheduleReminder(sanction.duration,TimeUnit.MINUTES,toBanish);
    }


    public void scheduleReminder(final long delay, final TimeUnit timeUnit, Member sanctionedMember) {


        final Runnable runnable = () -> notifyStaff(sanctionedMember);

        scheduledExecutorService.schedule(runnable, delay, timeUnit);

    }

    private void notifyStaff(Member sanctionedMember){

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));

        embedBuilder.setTitle(" :mute: Member automatisch ungemutet");
        embedBuilder.setAuthor(sanctionedMember.getUser().getAsTag(), null,sanctionedMember.getUser().getEffectiveAvatarUrl());
        embedBuilder.addField("Ungemuteter Member", sanctionedMember.getAsMention(), true);
        embedBuilder.addField("Grund",  "```Ende der Mutedauer```", false);
        embedBuilder.setTimestamp(Instant.now());

        Config.getInstance().getChannelConfig().getAutoModerationChannel().sendMessageEmbeds(embedBuilder.build()).queue();

    }
    @Override
    public boolean requiresAdmin() {
        return false;
    }


    private void notifyMutedUser(final User sanctionedUser, final SanctionType sanctionType, final String performedSanction, final String reason, final long duration, final Member sanctionAuthor) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        try {

            Helper.createEmbed(embedBuilder, String.valueOf(sanctionType), "Du wurdest auf **GoodQuestion** " + " **" + performedSanction + "**", EmbedColorHelper.MUTE);
            embedBuilder.addField("Dauer", duration + " Tage", true);
            embedBuilder.addField("Gemutet von", sanctionAuthor.getAsMention(), true);
            embedBuilder.addField("Grund", "```" + reason + "```", false);
            embedBuilder.setFooter(sanctionAuthor.getUser().getAsTag(), sanctionAuthor.getUser().getEffectiveAvatarUrl());
            embedBuilder.setTimestamp(Instant.now());

            sanctionedUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                    .complete();
        } catch (ErrorResponseException errorResponseException) {

            Objects.requireNonNull(Config.getInstance().getGuild()
                    .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                    .sendMessage(errorResponseException.getMessage() + " " +  sanctionedUser.getAsTag()).queue();

            System.out.println(errorResponseException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(errorResponseException, UserBanishCommand.class.getName()));
        }
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "`mute <user> <dauer> <reason>`: Schickt den User in Timeout";
    }

    @Override
    public boolean isPermitted(Member member) {
        return super.isPermitted(member);
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}