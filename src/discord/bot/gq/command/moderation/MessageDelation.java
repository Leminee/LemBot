package discord.bot.gq.command.moderation;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class MessageDelation extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] clearCommand = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();


        if (clearCommand[0].equalsIgnoreCase(Helper.PREFIX + "clear") && clearCommand[1].equalsIgnoreCase("-")) {

            if (clearCommand.length < 3) {

                EmbedBuilder howToUse = new EmbedBuilder();
                howToUse.setColor(0x8EE5EE);
                howToUse.setTitle("Hilfe");
                howToUse.setDescription("Richtige Command " + "-> " + Helper.PREFIX + "clear - [Anzahl der zu löschenden Nachrichten als Zahl]");
                event.getChannel().sendMessage(howToUse.build()).queue();

            }

            assert authorCommand != null;
            if (!authorCommand.hasPermission(Permission.MESSAGE_MANAGE)) {

                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff0000);
                error.setTitle("Permission Denied");
                event.getChannel().sendMessage(error.build()).queue();

            } else {

                try {

                    int messagesToDelete = Integer.parseInt(clearCommand[2]) + 1;

                    List<Message> messages = event.getChannel().getHistory().retrievePast(messagesToDelete).complete();

                    if (messagesToDelete >= 31) {

                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff0000);
                        error.setTitle("Lösche bitte nicht mehr als 30 Nachrichten auf einmal!");
                        event.getChannel().sendMessage(error.build()).queue();

                    } else {

                        event.getChannel().deleteMessages(messages).queue();

                        EmbedBuilder deletedSuccess = new EmbedBuilder();
                        deletedSuccess.setColor(0x00ff60);
                        deletedSuccess.setTitle("Bestätigung");
                        deletedSuccess.setDescription("Es wurden " + clearCommand[2] + " Nachrichten durch " + authorCommand.getAsMention() + " erfolgreich gelöscht!");
                        event.getChannel().sendMessage(deletedSuccess.build()).queue();
                    }

                } catch (IllegalArgumentException e) {
                    if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {

                        EmbedBuilder notSuccess = new EmbedBuilder();
                        notSuccess.setColor(0xff0000);
                        notSuccess.setTitle("Mehr als 100 Nachrichten können nicht gelöscht werden!");
                        event.getChannel().sendMessage(notSuccess.build()).queue();

                    }
                }
            }
        }

    }
}

