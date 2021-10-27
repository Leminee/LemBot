package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;

import java.awt.*;
import java.sql.SQLException;

public class TopPingedUserCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        try {
            channel.sendMessage(
                QueryHelper.getTopPingedUser()
                    .setTitle("Die am häufigsten gepingten User")
                    .setColor(Color.PINK)
                    .setThumbnail("https://s3.getstickerpack.com/storage/uploads/sticker-pack/pepe-and-peepo/sticker_17.png?42e3643fff4b46badc674bd2956e79af&d=200x200")
                    .build()
            ).queue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "topp";
    }

    @Override
    public String getDescription() {
        return "`?topp`: Zeigt die am meisten gepingten User an";
    }
}
