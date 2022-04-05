package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import tech.goodquestion.lembot.command.impl.moderation.MuteCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;
import java.util.List;

public class FanboyCommand implements IBotCommand {
    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {
        Member member = MuteCommand.getMemberFromCommandInput(message, args);
        String memberNickname = member.getNickname() == null ? member.getUser().getName():member.getNickname();
        sender.modifyNickname(memberNickname+"_fanboy").queue();
        Role role = sender.getGuild().createRole().setName(memberNickname+"_fanboy").complete();
        sender.getGuild().addRoleToMember(sender, role).queue();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Du bist nun der fanboy von " + member.getAsMention());

        message.replyEmbeds(embedBuilder.build()).queue();


    }


    @Override
    public String getName() {
        return "fb";
    }

    @Override
    public String getDescription() {
        return "`fb`: macht dich zum fanboy";
    }
    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}