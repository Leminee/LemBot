package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public final class MemberLogsCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {

        if (Helper.isStaffChannel(message, channel)) return;

        User user;
        try {
            user = Helper.getUserFromCommandInput(message, args);
        } catch (NumberFormatException numberFormatException) {
            Helper.sendError(message, ":x: Command nicht valid!");
            return;
        }

        final long userId = user.getIdLong();
        final String userAsMention = user.getAsMention();
        final String avatarUrl = user.getEffectiveAvatarUrl();
        final String userTag = user.getAsTag();
        final String accountCreationDate = LocalDateTimeFormatter.toGermanFormat(user.getTimeCreated().toLocalDateTime());

        message.getGuild().retrieveMember(user).queue(member -> {

            String lastActivityDateTimeRaw = String.valueOf(QueryHelper.getLastActivityDateTimeBy(userId));
            String lastActivityDateTime = (lastActivityDateTimeRaw == null) ? "N/A" : LocalDateTimeFormatter.toGermanFormat(LocalDateTime.parse(Objects.requireNonNull(lastActivityDateTimeRaw)));
            String lastActivity = !member.getOnlineStatus().equals(OnlineStatus.OFFLINE)
                    ? ":green_circle: Online"
                    : String.format("```js\nZuletzt aktiv am %s```", lastActivityDateTime);

            final long amountMessages = QueryHelper.getAmountMessagesBy(userId);
            final long amountBumps = QueryHelper.getAmountBumpsBy(userId);
            final int amountRoles = member.getRoles().size();

            final long warns = QueryHelper.getAmountWarns(userId);
            final long mutes = QueryHelper.getAmountTimeout(userId);
            final long bans = QueryHelper.getAmountBans(userId);

            String inviter = QueryHelper.getInviter(userId);
            String activeSanction = member.isTimedOut() ? "*TimedOut*" : "*Keine*";

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Member Logs");
            embedBuilder.setColor(Color.decode(EmbedColorHelper.MEMBER_INFO));
            embedBuilder.setAuthor(userTag, null, avatarUrl);
            embedBuilder.setThumbnail(user.getAvatarUrl());
            embedBuilder.addField("Member", userAsMention, true);
            embedBuilder.addField("Erstellungsdatum", accountCreationDate.replace("um", ""), true);
            embedBuilder.addField("Letztes Beitrittsdatum", getLastJoinDate(member).replace("um", ""), true);
            embedBuilder.addField("Nachrichten", String.valueOf(amountMessages), true);
            embedBuilder.addField("Bumps", String.valueOf(amountBumps), true);
            embedBuilder.addField("Rollen", String.valueOf(amountRoles), true);
            embedBuilder.addField(":warning: Verwarnungen", String.valueOf(warns), true);
            embedBuilder.addField(":hourglass: Timeouts", String.valueOf(mutes), true);
            embedBuilder.addField(":no_entry: Bans", String.valueOf(bans), true);
            embedBuilder.addField("Eingeladen von", inviter, true);
            embedBuilder.addField("Aktive Sanktion", activeSanction, true);
            embedBuilder.addField("Aktivität", lastActivity, false);

            Helper.sendEmbed(embedBuilder, message, true);

        }, error -> {
            Helper.sendError(message, ":x: Kein Member gefunden!");
        });
    }

    private String getLastJoinDate(Member member) {
        return LocalDateTimeFormatter.toGermanFormat(member.getTimeJoined().toLocalDateTime());
    }

    @Override
    public String getName() {
        return "ml";
    }

    @Override
    public String getDescription() {
        return "`ml <member>`: Member Logs";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
