package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.lib.Helper;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttachmentStorage extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        final long userId = event.getMessage().getAuthor().getIdLong();

        if (event.getMessage().getAttachments().size() == 0) return;

        final long attachmentId = event.getMessage().getAttachments().get(0).getIdLong();
        final String attachmentName = event.getMessage().getAttachments().get(0).getFileName();
        final String attachmentUrl = event.getMessage().getAttachments().get(0).getUrl();
        final String attachmentExtension = event.getMessage().getAttachments().get(0).getFileExtension();
        final double attachmentSize = event.getMessage().getAttachments().get(0).getSize();
        final double attachmentSizeInKiloByte = attachmentSize / 1024;

        saveLocally(event.getMessage().getAttachments().get(0), userId, event.getMember(), event.getMessage());

        Connection connection = DatabaseConnector.openConnection();
        final String insertMessageData = "INSERT INTO user_attachment (id_discord,id_attachment, name, url, extension,size) VALUES (?,?,?,?,?,?);";

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
            CommandsHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

    private void saveLocally(Message.Attachment attachment, long userId, Member member, Message message) {
        attachment.downloadToFile("attachments/" + getFileSenderAsTag(member, userId, message) + "_" + Helper.getGermanDateTime() + "_" + attachment.getFileName())
                .thenAccept(File::getName)
                .exceptionally(t ->
                {
                    System.out.println(t.getMessage());
                    return null;
                });
    }


    private String getFileSenderAsTag(Member member, long userId, Message message) {

       final User user = CommandManager.getInstance().getJDA().retrieveUserById(userId, true).complete();

        if (user != null) {
            member = message.getGuild().retrieveMember(user).complete();
        }

        return member.getUser().getAsTag();
    }
}
