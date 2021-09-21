package tech.goodquestion.lembot.command.db;

import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TopPingedUser extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String topPingedUsersCheckCommand = "topp";
        String topPingedUsers = "SELECT content, COUNT(id_discord) FROM `user_message_content` WHERE content LIKE '<@!%' OR '<@I%' GROUP BY content HAVING COUNT(id_discord) > 1 ORDER BY COUNT(id_discord) DESC LIMIT 3";
        String embedTitle = "Die am häufigsten gepingten User";
        Color embedColor = Color.PINK;
        String embedThumbnail = "https://s3.getstickerpack.com/storage/uploads/sticker-pack/pepe-and-peepo/sticker_17.png?42e3643fff4b46badc674bd2956e79af&d=200x200";

        Helper.selectTop(
                topPingedUsersCheckCommand, event,
                topPingedUsers,
                embedTitle, embedColor, embedThumbnail);
    }
}

