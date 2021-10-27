package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.BotCommand;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entities.Sanction;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;
import java.util.List;

public abstract class UserBanishCommand implements BotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        if (args.length < 1) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Bitte gebe die ID des zu kickenden Users und den Grund für die Bestrafung an!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        if (args.length < 2) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Bitte gebe einen Grund an!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        if (channel.getIdLong() != Config.getInstance().getChannels().getSanctionChannel().getIdLong()) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Dieser Befehl kann in diesem Channel nicht ausgeführt werden!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        List<Member> mentionedMembers = msg.getMentionedMembers();
        Member member = null;

        if (mentionedMembers.size() > 0) {
            member = mentionedMembers.get(0);
        } else {
            User user = CommandManager.getInstance().getJDA().retrieveUserById(args[0], true).complete();

            if (user != null) {
                member = msg.getGuild().retrieveMember(user).complete();
            }
        }

        if (member == null) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "User ist nicht auf dem Server!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Admins/Moderatoren können nicht gekickt oder gebannt werden!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discoapp.com/attachments/819694809765380146/879230207763038228/Bildschirmfoto_2021-086.png");
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
        sanction.author = msg.getAuthor().getAsTag();
        sanction.reason = reason.toString();
        sanction.channelName = channel.getName();

        if (requiresAdmin() && !msg.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Permission Denied";
            Helper.createEmbed(embedError, "", embedDescription, Color.RED);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        banishUser(member, sanction, msg);
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
                .queue();
    }
}
