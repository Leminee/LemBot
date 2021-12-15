package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TopFlooderCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        Connection connection = DatabaseConnector.openConnection();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(QueryHelper.TOP_FLOODER)) {
            EmbedBuilder topFlooderEmbed = new EmbedBuilder();

            String embedTitle = "User mit den meisten Nachrichten";
            String embedDescription = "";
            String embedThumbnail = "https://cdn.discordapp.com/attachments/819694809765380146/844312789531230208/typing.png";
            String amountOf = "Nachrichten";

            Helper.addTopToEmbed(resultSet, topFlooderEmbed, embedTitle, embedDescription, embedThumbnail, EmbedColorHelper.FLOOD, channel, amountOf);

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public String getName() {
        return "topf";
    }

    @Override
    public String getDescription() {
        return "`?topf`: User mit den meisten Nachrichten";
    }
}
