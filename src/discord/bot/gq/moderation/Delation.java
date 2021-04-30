package discord.bot.gq.moderation;

import discord.bot.gq.BotMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.List;

public class Delation extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] clearCommand = event.getMessage().getContentRaw().split("\\s+");
        Member author = event.getMessage().getMember();


        if (clearCommand[0].equalsIgnoreCase(BotMain.PREFIX + "clear") && clearCommand[1].equalsIgnoreCase("-")) {

            if (clearCommand.length < 3) {

                EmbedBuilder howToUse = new EmbedBuilder();
                howToUse.setColor(0x00ffff);
                howToUse.setTitle("Hilfe");
                howToUse.setDescription("Richtige Command " + "-> " + BotMain.PREFIX + "clear - [Anzahl der zu löschenden Nachrichten als Zahl]");
                event.getChannel().sendMessage(howToUse.build()).queue();

            }

            assert author != null;
            if (!author.hasPermission(Permission.MESSAGE_MANAGE)) {

                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff0000);
                error.setTitle("Permission Denied");
                event.getChannel().sendMessage(error.build()).queue();

            } else {

                try {

                    List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(clearCommand[2])).complete();

                    if (Integer.parseInt(clearCommand[2]) >= 51) {

                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff0000);
                        error.setTitle("Lösche bitte nicht mehr als 50 Nachrichten!");
                        event.getChannel().sendMessage(error.build()).queue();

                    } else {

                        event.getChannel().deleteMessages(messages).queue();

                        EmbedBuilder successDeleted = new EmbedBuilder();
                        successDeleted.setColor(0x00ff60);
                        successDeleted.setTitle("Information");
                        successDeleted.setDescription("Es wurden " + clearCommand[2] + " Nachrichten durch " + author.getAsMention() + " erfolgreich gelöscht!");
                        event.getChannel().sendMessage(successDeleted.build()).queue();
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

