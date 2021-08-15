package discord.bot.gq.database;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeletedMessageStorage extends ListenerAdapter {

    @Override
    public void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {


        long idDeletedMessage = event.getMessageIdLong();

        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String deletedMessage = "INSERT INTO deleted_message (id_deleted_message) VALUES (?);";

        try {

            PreparedStatement pS = db.getConnection().prepareStatement(deletedMessage);

            pS.setLong(1, idDeletedMessage);

            pS.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
