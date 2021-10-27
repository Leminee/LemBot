package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;

import java.awt.*;
import java.sql.SQLException;

public class TopEmojiCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        try {
            channel.sendMessage(
                QueryHelper.getTopEmojis()
                    .setTitle("Die am häufigsten benutzten Emojis")
                    .setColor(Color.yellow)
                    .setThumbnail("https://cdn.discordapp.com/attachments/819694809765380146/872659266962587678/Bildschirmfoto_2021-08-05_um_03.56.07.png")
                    .build()
            ).queue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "tope";
    }

    @Override
    public String getDescription() {
        return "`?tope`: Zeigt die meistgenutzen Emojis an";
    }
}
