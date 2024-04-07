package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopUsedCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args)  {

        Connection connection = DatabaseConnector.openConnection();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(QueryHelper.TOP_USED_COMMANDS)) {

            final EmbedBuilder topUsedCommandEmbed = new EmbedBuilder();

            final String embedTitle = "Die TOP 5 benutzten Commands";
            topUsedCommandEmbed.setTitle(embedTitle);
            topUsedCommandEmbed.setColor(Color.decode(EmbedColorHelper.TOP_USED_COMMANDS));

            Helper.addTopCommandToEmbed(resultSet, topUsedCommandEmbed, message, "");

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

    @Override
    public String getName() {
        return "tuc";
    }

    @Override
    public String getDescription() {
        return "`tuc`: Die 5 am meisten benutzten Commands";
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