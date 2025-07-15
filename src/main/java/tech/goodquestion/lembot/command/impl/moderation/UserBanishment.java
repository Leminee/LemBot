package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.BotMain;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.library.Helper;

import java.util.Objects;

public abstract sealed class UserBanishment implements IBotCommand permits BanCommand, WarnCommand {

    @SuppressWarnings("null")
    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        if (args.length < 2) {
            Helper.sendError(message, ":x: Gebe einen Grund an!");
            return;
        }

        if (channel.getIdLong() != Config.getInstance().getChannelConfig().getSanctionChannel().getIdLong()) {
            Helper.sendError(message, ":x: Dieser Befehl kann nur in [channel] ausgeführt werden!".replace("[channel]", Config.getInstance().getChannelConfig().getSanctionChannel().getAsMention()));
            return;
        }

        User user;

        user = Helper.getUserFromCommandInput(message, args);


        Member member;
        try {

            member = Config.getInstance().getGuild().retrieveMember(user).complete();


            final boolean isThemSelf = member.getIdLong() == sender.getIdLong();
            if (isThemSelf) {
                Helper.sendError(message, ":x: Du kannst dich selbst nicht bannen!");
                return;
            }


            if (BotMain.jda.getSelfUser().getIdLong() == member.getIdLong()) {
                Helper.sendError(message, String.format(":x: %s kann nicht gebannt werden! %s", Config.getInstance().getBotConfig().getName(), "!"));
                return;
            }



            if (isStaff(member)) {
                Helper.sendError(message, ":x: Staff-Mitglieder können nicht über Command sanktioniert werden!");
                return;
            }

            final StringBuilder reason = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }

            final Sanction sanction = new Sanction();

            sanction.userId = user.getIdLong();
            sanction.userTag = user.getAsTag();
            sanction.userName = user.getName();
            sanction.author = message.getAuthor().getAsTag();
            sanction.reason = reason.toString();
            sanction.channelName = channel.getName();

            if (requiresAdmin() && !isStaff(member)) {
                Helper.sendError(message, ":x: Permission denied!");
                return;
            }


            banishUser(user, sanction, message);
        } catch (ErrorResponseException errorResponseException) {

            final StringBuilder reason = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }

            final Sanction sanction = new Sanction();

            sanction.userId = user.getIdLong();
            sanction.userTag = user.getAsTag();
            sanction.userName = user.getName();
            sanction.author = message.getAuthor().getAsTag();
            sanction.reason = reason.toString();
            sanction.channelName = channel.getName();

            if (requiresAdmin() && !isStaff(sender)) {
                Helper.sendError(message, ":x: Permission denied!");
                return;
            }


            banishUser(user, sanction, message);

        }

    }


    private boolean isStaff(Member member) {

        return member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("administrator")) || member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("moderator"));

    }
    public abstract void banishUser(User toBanish, Sanction sanction, Message originMessage);

    public abstract boolean requiresAdmin();

    @Override
    public String getHelpList() {
        return "staff";
    }
}