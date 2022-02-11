package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UnmuteCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        if (args.length < 1) {
            return;
        }

        final List<Member> mentionedMembers = message.getMentionedMembers();
        Member member;

        member = UserBanishCommand.getMember(message, args, mentionedMembers, null);

        if (member == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: User ist nicht auf dem Server!";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
            return;
        }

        member.getGuild().removeRoleFromMember(member.getIdLong(), Config.getInstance().getRole().getMuteRole()).queue();

        final EmbedBuilder embedBuilder= new EmbedBuilder();
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setDescription("User " + member.getAsMention() + " wurde durch " + message.getAuthor().getAsMention() + " erfolgreich **" + " ungemutet." + "**");

        Helper.sendEmbed(embedBuilder,message,false);

        Connection connection = DatabaseConnector.openConnection();

        String userMuted = "SELECT activ FROM muted_user WHERE id_discord = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(userMuted)) {

            preparedStatement.setLong(1, member.getIdLong());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {


                String userUnmute = "UPDATE muted_user SET activ = ? WHERE id_discord = ? ORDER BY muted_at DESC LIMIT 1;";

                PreparedStatement preparedStatementOne = connection.prepareStatement(userUnmute);

                preparedStatementOne.setString(1, "0");
                preparedStatementOne.setLong(2, member.getIdLong());

                preparedStatementOne.executeUpdate();
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }

    }

    @Override
    public String getName() {
        return "unmute";
    }

    @Override
    public String getDescription() {
        return "`?unmute <user> <reason>`: entfernt" + Config.getInstance().getRole().getMuteRole().getAsMention();
    }


    @Override
    public String getHelpList() {
        return "staff";
    }
}
