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

public final class TopFlooderCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        Connection connection = DatabaseConnector.openConnection();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(QueryHelper.TOP_FLOODER)) {
            final EmbedBuilder topFlooderEmbed = new EmbedBuilder();

            final String embedTitle = "User mit den meisten Nachrichten";
            final String embedDescription = "";
            final String embedThumbnail = "https://cdn.discordapp.com/attachments/943983785547030598/1226480321718124585/typing.png?ex=6624ebcf&is=661276cf&hm=db304851e769b8677e3ce165ba5fdf77613f5ff2bc3286014a2d06348c9d4cce&";
            final String amountOf = "Nachrichten";

            Helper.addTopToEmbed(resultSet, topFlooderEmbed, embedTitle, embedDescription, embedThumbnail, EmbedColorHelper.FLOOD, message, amountOf);

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

    @Override
    public String getName() {
        return "topf";
    }

    @Override
    public String getDescription() {
        return "`topf`: User mit den meisten Nachrichten";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }

    @Override
    public String getHelpList() {
        return "stats";
    }
}