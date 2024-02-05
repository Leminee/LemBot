package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;

public final class UnwarnCommand implements IBotCommand, RemovalBanishment {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

        removeSanction(message, args);

        Member member = MuteCommand.getMemberFromCommandInput(message,args);

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setAuthor(member.getUser().getAsTag(), null,member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription("**" + ":warning: Verwarnung rückgängig gemacht" + "**");
        embedBuilder.addField("Member",member.getAsMention() , true);
        embedBuilder.addField("Rückgängig gemacht von",sender.getAsMention(), true);
        embedBuilder.setFooter(sender.getUser().getAsTag(),sender.getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        Helper.sendEmbed(embedBuilder,message,false);


        //TODO delete the warn from the database

    }

    @SuppressWarnings("null")
    @Override
    public void removeSanction(Message message, String[] args) {

        Member member = MuteCommand.getMemberFromCommandInput(message, args);

        message.getGuild().removeRoleFromMember(member, Config.getInstance().getRoleConfig().getWarnRole()).queue();

    }


    @Override
    public String getName() {
        return "unwarn";
    }

    @Override
    public String getDescription() {
        return "`unwarn <member>`: Entfernt" + Config.getInstance().getRoleConfig().getWarnRole().getAsMention();
    }

    @Override
    public String getHelpList() {
        return "staff";
    }

}
