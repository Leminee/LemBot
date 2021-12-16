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
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.util.List;
import java.util.Objects;

public abstract class UserBanishCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        if (args.length < 1) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Bitte gebe die ID des zu kickenden Users und den Grund für die Bestrafung an!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        if (args.length < 2) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Bitte gebe einen Grund an!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        if (channel.getIdLong() != Config.getInstance().getChannel().getSanctionChannel().getIdLong()) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Dieser Befehl kann nur in [channel] ausgeführt werden!".replace("[channel]",Config.getInstance().getChannel().getSanctionChannel().getAsMention());
            Helper.createEmbed(embedError, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessage(embedError.build()).queue();
            return;
        }


        List<Member> mentionedMembers = message.getMentionedMembers();
        Member member;

        try {

            member = getMember(message, args, mentionedMembers, null);

        } catch (ErrorResponseException errorResponseException) {

            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "User ist nicht auf dem Server!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessage(embedError.build()).queue();
            return;

        }

        assert member != null;
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Admins/Moderatoren können nicht gekickt oder gebannt werden!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        StringBuilder reason = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }

        Sanction sanction = new Sanction();

        sanction.userId = member.getIdLong();
        sanction.userTag = member.getUser().getAsTag();
        sanction.userName = member.getUser().getName();
        sanction.author = message.getAuthor().getAsTag();
        sanction.reason = reason.toString();
        sanction.channelName = channel.getName();

        if (requiresAdmin() && !Objects.requireNonNull(message.getMember()).hasPermission(Permission.MANAGE_ROLES)) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Verweigert";
            Helper.createEmbed(embedError, "", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        banishUser(member, sanction, message);
    }

    public static Member getMember(Message message, String[] args, List<Member> mentionedMembers, Member member) {
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

    public abstract void banishUser(Member toBanish, Sanction sanction, Message originMsg);
    public abstract boolean requiresAdmin();

    @Override
    public boolean isPermitted(Member member) {
        return member.hasPermission(Permission.MESSAGE_MANAGE);
    }

    @Override
    public String getHelpList() {
        return "staff";
    }

    public static void sendSanctionReason(User sanctionedUser, String typeSanction, String reason, String mentionedUser) {
        String content = "Du wurdest aus dem folgenden Grund auf GoodQuestion " + "**" + typeSanction + "**" + ": " + reason;
        sanctionedUser.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .complete();
    }
}
