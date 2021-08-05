package discord.bot.gq.command.moderation;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserBanishment extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived (@NotNull GuildMessageReceivedEvent event) {

        String[] kickCommand = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();



        if (kickCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") && !kickCommand[1].isEmpty()) {

            if (kickCommand.length < 3) {

                EmbedBuilder howToUse = new EmbedBuilder();
                howToUse.setColor(0x00ffff);
                howToUse.setTitle("Hilfe");
                howToUse.setDescription("Richtige Command " + "-> " + Helper.PREFIX + "kick [User ID] [Grund des Kicks] ");
                event.getChannel().sendMessage(howToUse.build()).queue();

            }

            assert authorCommand != null;
            if (!authorCommand.hasPermission(Permission.MESSAGE_MANAGE)) {

                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff0000);
                error.setTitle("Permission Denied");
                event.getChannel().sendMessage(error.build()).queue();

            } else {

                event.getMessage().delete().queue();

                if (event.getJDA().getUserById(kickCommand[1]) == null) {

                    EmbedBuilder confirmation = new EmbedBuilder();
                    confirmation.setColor(0xff0000);
                    confirmation.setTitle("Error");
                    confirmation.setDescription("User ist nicht auf dem Server");
                    event.getChannel().sendMessage(confirmation.build()).queue();

                    return;
                }


                String kickedUser = Objects.requireNonNull(event.getJDA().getUserById(kickCommand[1])).getAsTag();

                Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getGuild().getMemberById(kickCommand[1])).kick(kickCommand[2]))).queue();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + "*" + kickedUser + "* " + " wurde durch " + authorCommand.getAsMention() + " gekickt."
                        + "\n Angegebener Grund: " + kickCommand[2]);
                event.getChannel().sendMessage(confirmation.build()).queue();

            }
        }
    }
}
