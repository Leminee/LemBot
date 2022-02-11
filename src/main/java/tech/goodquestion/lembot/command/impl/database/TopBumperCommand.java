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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopBumperCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        Connection connection = DatabaseConnector.openConnection();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(QueryHelper.TOP_BUMPER)) {

            final EmbedBuilder topBumperEmbed = new EmbedBuilder();

            final String embedTitle = "User mit den meisten Bumps";
            final String embedDescription = "";
            final String embedThumbnail = "https://cdn.discordapp.com/attachments/819694809765380146/869248076446052402/Bildschirmfoto_2021-07-26_um_17.48.08.png";
            final String amountOf = "Bumps";

            Helper.addTopToEmbed(resultSet, topBumperEmbed, embedTitle, embedDescription, embedThumbnail, EmbedColorHelper.BUMP, message,amountOf);

        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

    @Override
    public String getName() {
        return "topb";
    }

    @Override
    public String getDescription() {
        return "`topb`: User mit den meisten Bumps";
    }

    @Override
    public boolean isPermitted(final Member member){
        return true;
    }
}
