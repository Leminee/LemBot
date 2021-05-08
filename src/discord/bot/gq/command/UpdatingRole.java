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

public class UpdatingRole extends ListenerAdapter {


    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] userMessage = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();
        String setConfigCommand = "setrid";

        if ((userMessage[0].equalsIgnoreCase(BotMain.PREFIX + setConfigCommand))) {

            if (userMessage.length != 2) {
                return;
            }

            if (!Objects.requireNonNull(authorCommand).hasPermission(Permission.ADMINISTRATOR)) {
                return;
            }
            ConfigSelection configSelection = new ConfigSelection();
            ConfigUpdating configUpdating = new ConfigUpdating();

            configSelection.setRoleId(userMessage[1]);
            configUpdating.updateRoleId();


            EmbedBuilder successUpdatedRoleId = new EmbedBuilder();
            successUpdatedRoleId.setColor(0x00ff60);
            successUpdatedRoleId.setTitle("Confirmation");
            successUpdatedRoleId.setDescription("ID der zu pingenden Rolle wurde erfolgreich bearbeitet " + authorCommand.getAsMention() + "!");
            event.getChannel().sendMessage(successUpdatedRoleId.build()).queue();


        }
    }
}
