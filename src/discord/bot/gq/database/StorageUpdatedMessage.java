package discord.bot.gq.database;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StorageUpdatedMessage extends ListenerAdapter {
    @Override
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {


        long idUpdatedMessage = event.getMessageIdLong();
        long authorId = event.getAuthor().getIdLong();
        String authorUpdatedMessage = event.getAuthor().getAsTag();
        String updatedMessageContent = event.getMessage().getContentRaw();


        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String insertUpdatedMessageData = "INSERT INTO updated_message (id_updated_message, id_message, id_discord, username,content) VALUES (NULL,?,?,?,?);";

        try {

            PreparedStatement pS = db.getConnection().prepareStatement(insertUpdatedMessageData);

            pS.setLong(1, idUpdatedMessage);
            pS.setLong(2, authorId);
            pS.setBlob(3, Helper.changeCharacterEncoding(pS, authorUpdatedMessage));
            pS.setBlob(4, Helper.changeCharacterEncoding(pS, updatedMessageContent));

            pS.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}

