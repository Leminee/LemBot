package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.util.Objects;

public abstract sealed class UserBanishment implements IBotCommand permits BanCommand, WarnCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        if (args.length < 2) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Gebe einen Grund an!";
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

        User user;

        user = Helper.getUserFromCommandInput(message, args);

        Member member = Config.getInstance().getGuild().retrieveMember(user).complete();

        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Admins/Moderatoren können nicht gebannt werden!";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
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

        if (requiresAdmin() && !Objects.requireNonNull(message.getMember()).hasPermission(Permission.MANAGE_ROLES)) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Permission denied";
            Helper.createEmbed(embedBuilder, "", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }


        banishUser(user, sanction, message);
    }


    public abstract void banishUser(User toBanish, Sanction sanction, Message originMessage);

    public abstract boolean requiresAdmin();

    @Override
    public String getHelpList() {
        return "staff";
    }
}
