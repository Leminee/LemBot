package discord.bot.gq.config.command;

import discord.bot.gq.config.db.ConfigUpdating;
import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UpdatingChannel extends ListenerAdapter {

    public static String channelId;

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] userMessageContent = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();
        String configCommand = "setcid";

        if ((userMessageContent[0].equalsIgnoreCase(Helper.PREFIX + configCommand))) {

            if (userMessageContent.length != 2) {
                return;
            }

            if (!Objects.requireNonNull(authorCommand).hasPermission(Permission.ADMINISTRATOR)) {
                return;
            }

            channelId = userMessageContent[1];

            ConfigUpdating configUpdating = new ConfigUpdating();
            configUpdating.updateChannelId();

            EmbedBuilder successUpdatedChannelId = new EmbedBuilder();
            successUpdatedChannelId.setColor(0x00ff60);
            successUpdatedChannelId.setTitle("Bestätigung");
            successUpdatedChannelId.setDescription("ID Channel wurde erfolgreich bearbeitet " + authorCommand.getAsMention() + "!");
            event.getChannel().sendMessage(successUpdatedChannelId.build()).queue();

        }
    }
}
