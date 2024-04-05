package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public final class CodeBlockHelpCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String highlightedCodeBlock = "\n\\```<Programmiersprache>\n // Code\n```";
        final String title = "Farbige Codeblöcke";
        final String description = String.format("So sendest du farbige Codeblöcke:\n%s", highlightedCodeBlock);


        Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.HIGHLIGHTED_CODE_BLOCK);
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/844681147203977248/985018234262659132/Bildschirmfoto_2022-06-11_um_05.10.40.png");

        Helper.sendEmbed(embedBuilder, message, false);
    }

    @Override
    public String getName() {
        return "hcb";
    }

    @Override
    public String getDescription() {
        return "`hcb`: Farbige Codeblöcke ";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}