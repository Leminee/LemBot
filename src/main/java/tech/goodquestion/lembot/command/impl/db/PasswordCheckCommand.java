package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordCheckCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.length != 1) {
            return;
        }

        message.delete().queue();
        final String userPassword = args[0];
        final String embedTitle = "Passwort-Sicherheitsüberprüfung";

        if (hasBeenLeaked(userPassword)) {

            String description = ":red_circle: Passwort wurde leider gefunden " + message.getAuthor().getAsMention();


            Helper.createEmbed(embedBuilder, embedTitle, description, EmbedColorHelper.ERROR);
        } else {

            String description = ":green_circle: Nicht gefunden " +message.getAuthor().getAsMention();

            Helper.createEmbed(embedBuilder, embedTitle,description, EmbedColorHelper.SUCCESS);
        }

        channel.sendMessage(embedBuilder.build()).queue();
    }

    public boolean hasBeenLeaked(final String userPassword) {

        if (userPassword.length() < 8) {
            return true;
        }

        Connection connection = DatabaseConnector.openConnection();

        final String passwordCheck = "SELECT pass FROM leaked_password WHERE pass = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(passwordCheck)) {

            preparedStatement.setString(1, userPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
        return false;
    }

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String getDescription() {
        return "`?check <password>`: Passwort-Sicherheitsüberprüfung";
    }
}
