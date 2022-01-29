package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.util.List;
import java.util.Objects;

public abstract sealed class UserBanishCommand implements IBotCommand permits BanCommand, WarnCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {


        if (args.length < 2) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Bitte gebe einen Grund an!";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        if (channel.getIdLong() != Config.getInstance().getChannel().getSanctionChannel().getIdLong()) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Dieser Befehl kann nur in [channel] ausgeführt werden!".replace("[channel]",Config.getInstance().getChannel().getSanctionChannel().getAsMention());
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }


        final List<Member> mentionedMembers = message.getMentionedMembers();
        Member member;

        try {

            member = getMember(message, args, mentionedMembers, null);

        } catch (ErrorResponseException errorResponseException) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: User ist nicht auf dem Server!";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;

        }

        assert member != null;
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Admins/Moderatoren können nicht gekickt oder gebannt werden!";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        final StringBuilder reason = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        final Sanction sanction = new Sanction();

        sanction.userId = member.getIdLong();
        sanction.userTag = member.getUser().getAsTag();
        sanction.userName = member.getUser().getName();
        sanction.author = message.getAuthor().getAsTag();
        sanction.reason = reason.toString();
        sanction.channelName = channel.getName();

        if (requiresAdmin() && !Objects.requireNonNull(message.getMember()).hasPermission(Permission.MANAGE_ROLES)) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Permission denied";
            Helper.createEmbed(embedBuilder, "", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        banishUser(member, sanction, message);
    }

    static Member getMember(Message message, String[] args, List<Member> mentionedMembers, Member member) {
        if (mentionedMembers.size() > 0) {
            member = mentionedMembers.get(0);
        } else {
            User user = CommandManager.getInstance().getJDA().retrieveUserById(args[0], true).complete();

            if (user != null) {
                member = message.getGuild().retrieveMember(user).complete();
            }
        }
        return member;
    }

    public abstract void banishUser(Member toBanish, Sanction sanction, Message originMessage);
    public abstract boolean requiresAdmin();

    @Override
    public boolean isPermitted(Member member) {
        return member.hasPermission(Permission.MESSAGE_MANAGE);
    }

    @Override
    public String getHelpList() {
        return "staff";
    }

    public static void sendSanctionReason(User sanctionedUser, SanctionType sanctionType, String performedSanction, String reason) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        try {
            Helper.createEmbed(embedBuilder, String.valueOf(sanctionType), "Du wurdest auf **GoodQuestion** " + " **" + performedSanction + "**" + "\n Grund: " + reason, EmbedColorHelper.ERROR);
            sanctionedUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                    .complete();
        }catch (ErrorResponseException errorResponseException) {
            System.out.println(errorResponseException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(errorResponseException,UserBanishCommand.class.getName()));
        }
    }
}
