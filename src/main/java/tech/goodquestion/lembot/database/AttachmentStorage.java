package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.entity.OccurredException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public final class AttachmentStorage extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        if (event.getMessage().getAttachments().size() == 0) return;

        final long userId = event.getMessage().getAuthor().getIdLong();
        final long attachmentId = event.getMessage().getAttachments().get(0).getIdLong();
        final String attachmentName = event.getMessage().getAttachments().get(0).getFileName();
        final String attachmentUrl = event.getMessage().getAttachments().get(0).getUrl();
        final String attachmentExtension = event.getMessage().getAttachments().get(0).getFileExtension();
        final double attachmentSize = event.getMessage().getAttachments().get(0).getSize();

        saveLocally(event.getMessage().getAttachments().get(0), event.getMember(), event.getMessage());

        Connection connection = DatabaseConnector.openConnection();
        final String insertMessageData = "INSERT INTO user_attachment (id_discord,id_attachment, name, url, extension,size) VALUES (?,?,?,?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageData)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, attachmentId);
            preparedStatement.setString(3, attachmentName);
            preparedStatement.setString(4, attachmentUrl);
            preparedStatement.setString(5, attachmentExtension);
            preparedStatement.setDouble(6, attachmentSize);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

    private void saveLocally(final Message.Attachment attachment, final Member member, final Message message) {

        final String fileName = getFileSenderAsTag(member, message) + "_" + getGermanDateTimeForFileName() + "_" + attachment.getFileName();
        attachment.downloadToFile("attachments/" + fileName);
    }

    private String getFileSenderAsTag(Member member, final Message message) {

        final User user = CommandManager.getInstance().getJDA().retrieveUserById(member.getIdLong(), true).complete();

        if (user != null) {
            member = message.getGuild().retrieveMember(user).complete();
        }
        return member.getUser().getAsTag();
    }

    private String getGermanDateTimeForFileName() {

        return LocalDateTime.now().getDayOfMonth()
                + "-" + LocalDateTime.now().getMonth().getValue()
                + "-" + LocalDateTime.now().getYear()
                + "_" + LocalDateTime.now().getHour()
                + ":" + LocalDateTime.now().getMinute();
    }
}
