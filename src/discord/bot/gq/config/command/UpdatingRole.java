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

public class UpdatingRole extends ListenerAdapter {

    public static String roleId;

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] userMessageContent = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();
        String configCommand = "setrid";
        boolean isAdmin = Objects.requireNonNull(authorCommand).hasPermission(Permission.ADMINISTRATOR);

        if (userMessageContent[0].equalsIgnoreCase(Helper.PREFIX + configCommand)) {

            if (userMessageContent.length != 2) {
                return;
            }

            if (!isAdmin) {
                return;
            }


            roleId = userMessageContent[1];
            ConfigUpdating configUpdating = new ConfigUpdating();
            configUpdating.updateRoleId();


            EmbedBuilder successUpdatedRoleId = new EmbedBuilder();
            successUpdatedRoleId.setColor(0x00ff60);
            successUpdatedRoleId.setTitle("Bestätigung");
            successUpdatedRoleId.setDescription("ID der zu pingenden Rolle wurde erfolgreich bearbeitet " + authorCommand.getAsMention() + "!");
            event.getChannel().sendMessage(successUpdatedRoleId.build()).queue();

        }
    }
}
