package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import javax.annotation.Nonnull;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

public class UpdatedMessageStorage extends ListenerAdapter {
    @Override
    public void onMessageUpdate(@Nonnull final MessageUpdateEvent event) {

        final long updatedMessageId = event.getMessageIdLong();
        final long authorId = event.getAuthor().getIdLong();
        final String authorUpdatedMessageAsTag = event.getAuthor().getAsTag();
        final String updatedMessageContent = event.getMessage().getContentRaw();

        Connection connection = DatabaseConnector.openConnection();
        final String updatedMessageData = "INSERT INTO updated_message (id, id_message, id_discord, username,content) VALUES (NULL,?,?,?,?);";

        final String updatedMessageAuthorAsMention = "<@" + authorId+ ">" ;
        final String channelAsMention = "<#" + event.getChannel().getIdLong() +">";
        final String newContent = updatedMessageContent;
        final String authorAvatarUrl = event.getAuthor().getEffectiveAvatarUrl();


        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Bearbeitete Nachricht")
                .setAuthor(authorUpdatedMessageAsTag, null,authorAvatarUrl)
                .setColor(Color.decode(EmbedColorHelper.UPDATED))
                .addField("Author", updatedMessageAuthorAsMention,true)
                .addField("Author ID", String.valueOf(authorId),true)
                .addField("Kanal",channelAsMention,true)
                .addField("Alter Inhalt",QueryHelper.getUpdatedMessageOldContent(updatedMessageId, QueryHelper.UPDATED_MESSAGE_LAST_CONTENT),false)
                .addField("Neuer Inhalt", newContent,false)
                .setTimestamp(Instant.now());

        Config.getInstance().getChannel().getUpdatedDeletedChannel().sendMessageEmbeds(embedBuilder.build()).queue();


        try (PreparedStatement preparedStatement = connection.prepareStatement(updatedMessageData)) {
            preparedStatement.setLong(1, updatedMessageId);
            preparedStatement.setLong(2, authorId);
            preparedStatement.setBlob(3, Helper.changeCharacterEncoding(preparedStatement, authorUpdatedMessageAsTag));
            preparedStatement.setBlob(4, Helper.changeCharacterEncoding(preparedStatement, updatedMessageContent));

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

}

