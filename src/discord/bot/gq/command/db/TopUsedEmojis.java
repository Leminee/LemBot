package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TopUsedEmojis extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        
        String topUsedEmojisCheckCommand = "tope";
        String embedTitle = "Die am häufigsten benutzten Emojis";
        Color embedColor = Color.yellow;
        String embedThumbnail = "https://cdn.discordapp.com/attachments/819694809765380146/872659266962587678/Bildschirmfoto_2021-08-05_um_03.56.07.png";

        String topUsedEmojis =     """
                        SELECT '\uD83D\uDC4D', SUM(id_discord) AS c  FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDC4D%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T
                        UNION ALL\s
                        SELECT '\uD83D\uDE05', SUM(id_discord) AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE05%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL  \s
                        SELECT '\uD83D\uDE0A', SUM(id_discord )AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE0A%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDC40', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDC40%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE02', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE02%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE09', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE09%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE1B', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE1B%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T \s
                        UNION ALL
                        SELECT content, SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE42%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83D\uDE42', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE43%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        UNION ALL
                        SELECT '\uD83E\uDD13', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83E\uDD13%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T \s
                        UNION ALL
                        SELECT '\uD83D\uDE04', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE04%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
                        ORDER BY c DESC LIMIT 3;""";

        Helper.selectTop(
                topUsedEmojisCheckCommand, event, topUsedEmojis
            ,embedTitle
                , embedColor, embedThumbnail);
    }
}