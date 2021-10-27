package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

public class CodeBlockHelpCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        channel.sendMessage("https://cdn.discordapp.com/attachments/819694809765380146/832676790875062272/farbiger_code.png").queue();
    }

    @Override
    public String getName() {
        return "hcb";
    }

    @Override
    public String getDescription() {
        return "`?hcb`: Zeigt, dir wie man farbige Codeblöcke sendet";
    }
}
