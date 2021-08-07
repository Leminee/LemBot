package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TopPingedUser extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        Helper.selectTop(
                "topu", event,
                "SELECT content, COUNT(id_discord) FROM `user_message_content` WHERE content LIKE '<@!%' OR '<@I%' GROUP BY content HAVING COUNT(id_discord) > 1 ORDER BY COUNT(id_discord) DESC LIMIT 3",
                "Die 3 am häufigsten gepingten User", Color.PINK, "https://s3.getstickerpack.com/storage/uploads/sticker-pack/pepe-and-peepo/sticker_17.png?42e3643fff4b46badc674bd2956e79af&d=200x200");
    }
}

