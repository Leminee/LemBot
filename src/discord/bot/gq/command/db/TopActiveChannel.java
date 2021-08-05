package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class TopActiveChannel extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        Helper.selectTop(
                "topc", event,
                "SELECT channel_name, COUNT(id_channel) FROM `channel` GROUP BY id_channel ORDER BY COUNT(id_channel) DESC LIMIT 3;",
                "Die 3 aktivsten Channels", Color.gray, "https://cdn.discordapp.com/attachments/819694809765380146/872673996280303616/Bildschirmfoto_2021-08-05_um_04.54.26.png");
    }

}
