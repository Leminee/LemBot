package discord.bot.gq.database;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatedMessageStorage extends ListenerAdapter {
    @Override
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {


        long idUpdatedMessage = event.getMessageIdLong();
        long authorId = event.getAuthor().getIdLong();
        String authorUpdatedMessage = event.getAuthor().getAsTag();
        String updatedMessageContent = event.getMessage().getContentRaw();


        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String updatedMessageData = "INSERT INTO updated_message (id_updated_message, id_message, id_discord, username,content) VALUES (NULL,?,?,?,?);";

        try(PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(updatedMessageData)) {


            preparedStatement.setLong(1, idUpdatedMessage);
            preparedStatement.setLong(2, authorId);
            preparedStatement.setBlob(3, Helper.changeCharacterEncoding(preparedStatement, authorUpdatedMessage));
            preparedStatement.setBlob(4, Helper.changeCharacterEncoding(preparedStatement, updatedMessageContent));

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

    }

}

