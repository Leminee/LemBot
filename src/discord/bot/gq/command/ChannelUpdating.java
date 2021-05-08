package discord.bot.gq.command;

import discord.bot.gq.BotMain;
import discord.bot.gq.database.config.ConfigSelection;
import discord.bot.gq.database.config.ConfigUpdating;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChannelUpdating extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] userMessage = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();
        String setConfig = "setcid";

        if ((userMessage[0].equalsIgnoreCase(BotMain.PREFIX + setConfig))) {

            if (userMessage.length != 2) {
                return;
            }

            if (!Objects.requireNonNull(authorCommand).hasPermission(Permission.ADMINISTRATOR)) {
                return;
            }

            ConfigSelection configSelection = new ConfigSelection();
            configSelection.setChannelId((userMessage[1]));

            ConfigUpdating configUpdating = new ConfigUpdating();
            configUpdating.updateChannelId();

            EmbedBuilder successUpdatedChannelId = new EmbedBuilder();
            successUpdatedChannelId.setColor(0x00ff60);
            successUpdatedChannelId.setTitle("Confirmation");
            successUpdatedChannelId.setDescription("ID Channel wurde erfolgreich bearbeitet " + authorCommand.getAsMention() + "!");
            event.getChannel().sendMessage(successUpdatedChannelId.build()).queue();

        }
    }
}
