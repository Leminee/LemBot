package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

public class BotSourceCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        EmbedBuilder botInfoEmbed = new EmbedBuilder()
                .setTitle("LemBot Informationen")
                .setColor(-9862987)
                .setThumbnail("https://cdn.discordapp.com/avatars/815894805896888362/e8ac27a6bda7b0846bf5135d39e14943.webp?size=128")
                .addField("Geschrieben in:", "Java (JDA)", false)
                .addField("Geschrieben von:", "Lemin(e)#5985", false)
                .addField("Source Code:", "https://github.com/Leminee/LemBot", false);
        channel.sendMessage(botInfoEmbed.build()).queue();
    }

    @Override
    public String getName() {
        return "bsource";
    }

    @Override
    public String getDescription() {
        return "`?bsource`: Zeigt Information zum Discord Bot an";
    }
}
