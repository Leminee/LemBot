package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.command.impl.AdvertisingCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class MemberLogsCommand implements IBotCommand {
    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {


        if (Helper.isStaffChannel(message, channel)) return;

        User user;

        try {
            user = Helper.getUserFromCommandInput(message, args);
        }

        catch (NumberFormatException numberFormatException) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            Helper.createEmbed(embedBuilder, "Error", ":x: Command nicht valid", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        final long userId = user.getIdLong();
        final String userAsMention = user.getAsMention();

        Member member;
        try {
            member = message.getGuild().retrieveMember(user).complete();

        } catch (ErrorResponseException errorResponseException) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            Helper.createEmbed(embedBuilder, "Error", ":x: Kein Member gefunden", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        final String accountCreationDate = LocalDateTimeFormatter.toGermanFormat(user.getTimeCreated().toLocalDateTime());

        final String lastActivityDateTime = QueryHelper.getLastActivityDateTimeBy(userId) == null
                ? "N/A"
                : LocalDateTimeFormatter.toGermanFormat(Objects.requireNonNull(QueryHelper.getLastActivityDateTimeBy(userId)));

        final long amountMessages = QueryHelper.getAmountMessagesBy(userId);
        final long amountBumps = QueryHelper.getAmountBumpsBy(userId);

        final String lastActivity = !member.getOnlineStatus().equals(OnlineStatus.OFFLINE)
                ? ":green_circle: Online"
                : String.format("```js\nZuletzt aktiv am %s```" ,lastActivityDateTime);

        final String activeSanction = QueryHelper.hasActiveSanction(userId) ? "*Mute*" : "*Keine*";

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Member Logs");
        embedBuilder.setColor(Color.decode(EmbedColorHelper.MEMBER_INFO));
        embedBuilder.setAuthor(user.getAsTag(), null, Helper.getUserFromCommandInput(message, args).getEffectiveAvatarUrl());
        embedBuilder.setThumbnail(user.getAvatarUrl());
        embedBuilder.addField("Member", userAsMention, true);
        embedBuilder.addField("Erstellungsdatum", accountCreationDate.replace("um",""), true);
        embedBuilder.addField("Letztes Beitrittsdatum", getLastJoinDate(member).replace("um",""), true);
        embedBuilder.addField("Nachrichten", String.valueOf(amountMessages), true);
        embedBuilder.addField("Bumps", String.valueOf(amountBumps), true);
        embedBuilder.addField("Rollen", String.valueOf(getAmountRoles(member)), true);
        embedBuilder.addField(":warning: Verwarnungen", String.valueOf(QueryHelper.getAmountWarns(userId)), true);
        embedBuilder.addField(":mute: Mutes", String.valueOf(QueryHelper.getAmountMutes(userId)), true);
        embedBuilder.addField(":no_entry: Bans", String.valueOf(QueryHelper.getAmountBans(userId)), true);
        embedBuilder.addField("Eingeladen von", QueryHelper.getInviter(userId), true);
        embedBuilder.addField("Aktive Sanktion", activeSanction, true);
        embedBuilder.addField("Aktivität", lastActivity, false);

        Helper.sendEmbed(embedBuilder, message, true);
    }

    private String getLastJoinDate(Member member){
        return LocalDateTimeFormatter.toGermanFormat(member.getTimeJoined().toLocalDateTime());
    }

    private int getAmountRoles(Member member){
        return member.getRoles().size();
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
