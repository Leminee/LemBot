package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.BotCommand;
import tech.goodquestion.lembot.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordCheckCommand implements BotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        if (args.length != 1) {
            return;
        }

        msg.delete().queue();
        String userPassword = args[0];

        if (checkPassword(userPassword)) {
            channel.sendMessage(" :red_circle:   Pwned - Passwort wurde gefunden! " + msg.getAuthor().getAsMention()).queue();
        } else {
            channel.sendMessage(" :green_circle:  Nicht gefunden! " + msg.getAuthor().getAsMention()).queue();
        }
    }

    public boolean checkPassword(String userPassword) {
        if (userPassword.length() < 8) {
            return true;
        }

        Connection conn = DatabaseConnector.openConnection();

        String passwordCheck = "SELECT pass FROM leaked_password WHERE pass = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(passwordCheck)) {

            preparedStatement.setString(1, userPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String getDescription() {
        return "`?check <password>`: überprüft, ob das Passwort sicher ist";
    }
}
