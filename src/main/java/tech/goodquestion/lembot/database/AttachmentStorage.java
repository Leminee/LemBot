package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttachmentStorage extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        long userId = event.getMessage().getAuthor().getIdLong();

        if (event.getMessage().getAttachments().size() == 0) return;

        long attachmentId = event.getMessage().getAttachments().get(0).getIdLong();
        String attachmentName = event.getMessage().getAttachments().get(0).getFileName();
        String attachmentUrl = event.getMessage().getAttachments().get(0).getUrl();
        String attachmentExtension = event.getMessage().getAttachments().get(0).getFileExtension();
        double attachmentSize = event.getMessage().getAttachments().get(0).getSize();
        double attachmentSizeInKiloByte = attachmentSize/1024;

        Connection connection = DatabaseConnector.openConnection();
        String insertMessageData = "INSERT INTO user_attachment (id_discord,id_attachment, name, url, extension,size) VALUES (?,?,?,?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageData)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, attachmentId);
            preparedStatement.setString(3, attachmentName);
            preparedStatement.setString(4, attachmentUrl);
            preparedStatement.setString(5, attachmentExtension);
            preparedStatement.setDouble(6, attachmentSizeInKiloByte);

            preparedStatement.executeUpdate();


        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }
}
