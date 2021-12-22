package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

public class CodeBlockHelpCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String highlightedCodeBlock = "\n\\```<Programmiersprache>\n // Code\n```";
        final String title = "Farbige Codeblöcke";
        final String description = String.format("So sendest du farbige Codeblöcke:\n%s", highlightedCodeBlock);


        Helper.createEmbed(embedBuilder, title,
                description,
                EmbedColorHelper.HIGHLIGHTED_CODE_BLOCK);
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/919074434021736507/923287641087176734/Bildschirmfoto_2021-12-22_um_19.55.21.png");

        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "hcb";
    }

    @Override
    public String getDescription() {
        return "`?hcb`: farbige Codeblöcke ";
    }
}