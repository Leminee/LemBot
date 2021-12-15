package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UnmuteCommand implements IBotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        if (args.length < 1) {
            return;
        }

        List<Member> mentionedMembers = msg.getMentionedMembers();
        Member member;

        member = UserBanishCommand.getMember(msg, args, mentionedMembers, null);

        if (member == null) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "User ist nicht auf dem Server!";
            Helper.createEmbed(embedError, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessage(embedError.build()).queue();
            return;
        }

        member.getGuild().removeRoleFromMember(member.getIdLong(), Config.getInstance().getRole().getMuteRole()).queue();

        EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(0x00ff60);
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + member.getAsMention() + " wurde durch " + msg.getAuthor().getAsMention() + " erfolgreich **" + " ungemutet." + "**");
        channel.sendMessage(confirmation.build()).queue();

        Connection conn = DatabaseConnector.openConnection();

        String userMuted = "SELECT activ FROM muted_user WHERE id_discord = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(userMuted)) {

            preparedStatement.setLong(1, member.getIdLong());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {


                String userUnmute = "UPDATE muted_user SET activ = ? WHERE id_discord = ? ORDER BY muted_on DESC LIMIT 1;";

                PreparedStatement preparedStatementOne = conn.prepareStatement(userUnmute);

                preparedStatementOne.setString(1, "0");
                preparedStatementOne.setLong(2, member.getIdLong());

                preparedStatementOne.executeUpdate();
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

    }

    @Override
    public String getName() {
        return "unmute";
    }

    @Override
    public String getDescription() {
        return "`?unmute <user>`: entfernt dem User " + Config.getInstance().getRole().getMuteRole().getAsMention();
    }

    @Override
    public boolean isPermitted(Member member) {
        return member.hasPermission(Permission.MESSAGE_MANAGE);
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
