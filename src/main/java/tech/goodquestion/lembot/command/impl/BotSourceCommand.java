package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.lib.EmbedColorHelper;

import java.awt.*;

public class BotSourceCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        EmbedBuilder botInfoEmbed = new EmbedBuilder()
                .setTitle("LemBot Information")
                .setColor(Color.decode(EmbedColorHelper.GOOD_QUESTION))
                .setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif")
                .addField("Geschrieben in:", "Java (JDA)", false)
                .addField("Geschrieben von:", "Lemine#5985", false)
                .addField("Source Code:", "https://github.com/Leminee/LemBot", false);
        channel.sendMessage(botInfoEmbed.build()).queue();
    }

    @Override
    public String getName() {
        return "lembot";
    }

    @Override
    public String getDescription() {
        return "`?lembot`: Informationen zum LemBot";
    }
}
