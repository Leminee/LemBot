package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.impl.moderation.MuteCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.io.IOException;

public class FanboyCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {


        final Member member = MuteCommand.getMemberFromCommandInput(message, args);
        final String memberNickname = member.getNickname() == null ? member.getUser().getName() : member.getNickname();

        sender.modifyNickname(memberNickname + "_fanboy").queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setDescription("Du bist nun der fanboy von " + member.getAsMention());
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));

        message.replyEmbeds(embedBuilder.build()).queue();


    }

    @Override
    public String getName() {
        return "fb";
    }

    @Override
    public String getDescription() {
        return "`fb <member>`: Macht dich zum fanboy vom Member";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}