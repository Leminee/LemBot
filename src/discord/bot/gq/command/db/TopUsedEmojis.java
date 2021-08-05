package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TopUsedEmojis extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived (@NotNull GuildMessageReceivedEvent event){
        Helper.selectTop(
                "tope", event,
                "SELECT content, COUNT(id_discord) FROM `user_message_content` WHERE content IN ('\uD83D\uDE02', '\uD83D\uDC4D', '\uD83D\uDE04','\uD83D\uDE1B','\uD83D\uDE0E','\uD83D\uDC40') GROUP BY content HAVING COUNT(id_discord) > 1 ORDER BY COUNT(id_discord) DESC LIMIT 3",
                "Die 3 am häufigsten benutzten Emojis", Color.YELLOW, "https://cdn.discordapp.com/attachments/819694809765380146/872659266962587678/Bildschirmfoto_2021-08-05_um_03.56.07.png");
    }
}
