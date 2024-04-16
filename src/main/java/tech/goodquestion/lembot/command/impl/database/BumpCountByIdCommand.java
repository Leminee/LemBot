package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.command.impl.moderation.MuteCommand;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BumpCountByIdCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException, SQLException {

        if (args.length < 1) return;

        final long userId = Long.parseLong(args[0]);

        Member member;
        try {

            member = MuteCommand.getMemberFromCommandInput(message, args);

        } catch (ErrorResponseException errorResponseException) {

            final EmbedBuilder embedBuilder1 = new EmbedBuilder();
            Helper.createEmbed(embedBuilder1, "Error", ":x: Kein Member gefunden", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder1, message, true);
            return;
        }

        final long amountBump = getAmountBumpsOf(userId);

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Bumps");
        embedBuilder.setColor(Color.decode(EmbedColorHelper.BUMP));
        embedBuilder.setDescription("Anzahl der Bumps von " + member.getAsMention() + " **" + amountBump + "**");

        Helper.sendEmbed(embedBuilder,message,true);

    }

    private long getAmountBumpsOf(final long userId) {

        Connection connection = DatabaseConnector.openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(QueryHelper.AMOUNT_BUMPS)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(1);
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, Helper.class.getName()));
        }
        return -1;
    }

    @Override
    public String getName() {
        return "abby";
    }

    @Override
    public String getDescription() {
        return "`abby <id>`: Anzahl der Bumps des Members";
    }

    @Override
    public boolean isPermitted(Member member) {
        return true;
    }

    @Override
    public String getHelpList() {
        return "stats";
    }
}