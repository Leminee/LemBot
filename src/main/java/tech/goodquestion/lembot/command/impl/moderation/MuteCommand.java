package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;
import tech.goodquestion.lembot.library.parser.LocalDateTimeDurationCalculator;
import tech.goodquestion.lembot.library.parser.LocalDateTimeParser;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class MuteCommand implements IBotCommand {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

        if (args.length < 3) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Gebe eine Dauer und einen Grund an!";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }


        if (channel.getIdLong() != Config.getInstance().getChannelConfig().getSanctionChannel().getIdLong()) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Dieser Befehl kann nur in [channel] ausgeführt werden!".replace("[channel]", Config.getInstance().getChannelConfig().getSanctionChannel().getAsMention());
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String performedSanction = SanctionType.MUTE.getVerbalizedSanctionTyp();

        Member sanctionedMember;
        try {

            sanctionedMember = getMemberFromCommandInput(message, args);

        } catch (ErrorResponseException errorResponseException){

            final EmbedBuilder embedBuilder1 = new EmbedBuilder();
            Helper.createEmbed(embedBuilder1, "Error", ":x: Kein Member gefunden", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder1, message, true);
            return;

        }

        Sanction sanction = new Sanction();
        sanction.author = sender.getUser().getAsTag();
        sanction.channelName = channel.getName();
        sanction.userName = sanctionedMember.getUser().getName();
        sanction.userId = sanctionedMember.getIdLong();
        sanction.userTag = sanctionedMember.getUser().getAsTag();

        StringBuilder duration = new StringBuilder();

        for (int i = 1; i < 3; i++) {
            duration.append(args[i]).append(" ");
        }

        sanction.duration = duration.toString();

        StringBuilder reason = new StringBuilder();

        for (int i = 3; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        sanction.reason = reason.toString();

        final long parsedDuration = parseDuration(sanction.duration);


        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setDescription("**" + ":mute: Member gemutet" + "**");

        embedBuilder.setAuthor(sanctionedMember.getUser().getAsTag(), null, sanctionedMember.getUser().getEffectiveAvatarUrl());
        embedBuilder.addField("Gemuteter Member", sanctionedMember.getUser().getAsMention(), true);
        embedBuilder.addField("Gemutet von", sender.getAsMention(), true);
        embedBuilder.addField("Dauer", sanction.duration, true);
        embedBuilder.addField("Grund", "```" + sanction.reason + "```", false);
        embedBuilder.setFooter(sender.getUser().getAsTag(), sender.getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());


        for (final Role role : sanctionedMember.getRoles()) {
            message.getGuild().removeRoleFromMember(sanctionedMember, role).queue();
        }

        message.getGuild().addRoleToMember(sanctionedMember, Config.getInstance().getRoleConfig().getMuteRole()).queue();

        Helper.sendEmbed(embedBuilder, message, false);

        notifyMutedUser(sanctionedMember, performedSanction, sanction.reason, sanction.duration, sender);

        CommandHelper.logMemberMute(sanction);

        scheduleReminder(parsedDuration, TimeUnit.MINUTES, sanctionedMember);
    }


    public void scheduleReminder(final long delay, final TimeUnit timeUnit, Member sanctionedMember) {

        final Runnable runnable = () -> {

            notifyStaff(sanctionedMember);
            sanctionedMember.getGuild().removeRoleFromMember(sanctionedMember,Config.getInstance().getRoleConfig().getMuteRole()).queue();

        };


        scheduledExecutorService.schedule(runnable, delay, timeUnit);
    }


    private void notifyStaff(Member sanctionedMember) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));
        embedBuilder.setTitle(" :mute: Member automatisch ungemutet");
        embedBuilder.setAuthor(sanctionedMember.getUser().getAsTag(), null, sanctionedMember.getUser().getEffectiveAvatarUrl());
        embedBuilder.addField("Ungemuteter Member", sanctionedMember.getAsMention(), true);
        embedBuilder.addField("Ungemuteter Member ID", String.valueOf(sanctionedMember.getIdLong()), true);
        embedBuilder.addField("Grund", "```Ende der Mutedauer```", false);
        embedBuilder.setTimestamp(Instant.now());

        Config.getInstance().getChannelConfig().getAutoModerationChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }


    private void notifyMutedUser(final Member sanctionedMember, final String performedSanction, final String reason, final String duration, final Member sanctionAuthor) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        try {

            Helper.createEmbed(embedBuilder, ":mute: " + SanctionType.MUTE, "Du wurdest auf **GoodQuestion** " + " **" + performedSanction + "**", EmbedColorHelper.MUTE);
            embedBuilder.addField("Dauer", duration, true);
            embedBuilder.addField("Gemutet von", sanctionAuthor.getAsMention(), true);
            embedBuilder.addField("Grund", "```" + reason + "```", false);
            embedBuilder.setFooter(sanctionAuthor.getUser().getAsTag(), sanctionAuthor.getUser().getEffectiveAvatarUrl());
            embedBuilder.setTimestamp(Instant.now());

            sanctionedMember.getUser().openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                    .complete();
        } catch (ErrorResponseException errorResponseException) {

            Objects.requireNonNull(Config.getInstance().getGuild()
                    .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                    .sendMessage(errorResponseException.getMessage() + " " + sanctionedMember.getUser().getAsTag()).queue();

            System.out.println(errorResponseException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(errorResponseException, this.getClass().getName()));
        }
    }


    public static Member getMemberFromCommandInput(final Message message, final String[] args) {

        List<Member> mentionedMembers = message.getMentionedMembers();
        Member member;

        if (mentionedMembers.size() == 0) {
            member = Config.getInstance().getGuild().retrieveMemberById(args[0], true).complete();
        } else {
            member = mentionedMembers.get(0);
        }

        return member;
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "`mute <member> <dauer> <reason>`: Mutet den Member";
    }


    @Override
    public String getHelpList() {
        return "staff";
    }

    private long parseDuration(String input) {

        return LocalDateTimeDurationCalculator.getDurationUntilInMinutes(Objects.requireNonNull(LocalDateTimeParser.parse(input)));

    }
}