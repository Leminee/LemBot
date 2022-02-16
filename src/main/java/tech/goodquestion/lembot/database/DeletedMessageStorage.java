package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import javax.annotation.Nonnull;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public final class DeletedMessageStorage extends ListenerAdapter {

    @Override
    public void onMessageDelete(@Nonnull final MessageDeleteEvent event) {

        Connection connection = DatabaseConnector.openConnection();

        final String messageStored = "SELECT id_message, id_discord, content FROM user_message_content WHERE id_message = ?";
        final String deletedMessage = "INSERT INTO deleted_message (id_deleted_message) VALUES (?);";

        final long idDeletedMessage = event.getMessageIdLong();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(messageStored);
            preparedStatement.setLong(1, idDeletedMessage);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                final String deletedMessageAuthorAsMention = "<@" + resultSet.getString(2) + ">";
                String deletedMessageContent = resultSet.getString(3);
                final String channelAsMention = "<#" + event.getChannel().getIdLong() + ">";

                final User authorDeletedMessage = event.getJDA().retrieveUserById(resultSet.getString(2)).complete();

                assert authorDeletedMessage != null;
                final String authorDeletedMessageAsTag = authorDeletedMessage.getAsTag();
                final String authorDeletedMessageAvatarUrl = authorDeletedMessage.getEffectiveAvatarUrl();
                final long authorDeletedMessageIdLong = authorDeletedMessage.getIdLong();


                final EmbedBuilder embedBuilder = new EmbedBuilder();

                if (!isValidLength(deletedMessageContent)) {

                    deletedMessageContent = deletedMessageContent.substring(0, 900)
                            + "\n\n ```Hinweis: Der Inhalt der gelöschten Nachricht war länger als die erlaubten Zeichen (1024).```";
                }

                // FIXME: 14.02.22 add deleted pic as Image (what abouts vids?)
                embedBuilder.setTitle("Gelöschte Nachricht")
                        .setAuthor(authorDeletedMessageAsTag, null, authorDeletedMessageAvatarUrl)
                        .setColor(Color.decode(EmbedColorHelper.DELETED))
                        .addField("Author", deletedMessageAuthorAsMention, true)
                        .addField("Author ID", String.valueOf(authorDeletedMessageIdLong), true)
                        .addField("Kanal", channelAsMention, true)
                        .addField("Inhalt", deletedMessageContent, false)
                        .setTimestamp(Instant.now());


                Config.getInstance().getChannelConfig().getUpdatedDeletedChannel().sendMessageEmbeds(embedBuilder.build()).queue();

                PreparedStatement preparedStatementOne = connection.prepareStatement(deletedMessage);
                preparedStatementOne.setLong(1, idDeletedMessage);
                preparedStatementOne.executeUpdate();
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

    private boolean isValidLength(final String message) {

        final int fieldMaxLength = 1024;
        return message.length() < fieldMaxLength;
    }
}
