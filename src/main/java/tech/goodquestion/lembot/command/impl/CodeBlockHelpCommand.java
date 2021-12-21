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

        EmbedBuilder embedBuilder = new EmbedBuilder();
        String highlightedCodeBlock = "\n\\```<Programmiersprache>\n // Code\n```";
        String title = "Farbige Codeblöcke";
        String description = String.format("So sendest du farbige Codeblöcke:\n%s", highlightedCodeBlock);


        Helper.createEmbed(embedBuilder, title,
                description,
                EmbedColorHelper.HIGHLIGHTED_CODE_BLOCK);
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/919074434915135532/921175814735810560/Bildschirmfoto_2021-12-17_um_00.03.43.png");

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