package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;

public final class UnbanCommand implements IBotCommand, RemovalBanishment {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {


        if (args.length < 1) return;
        
        removeSanction(message, args);

        User user = Helper.getUserFromCommandInput(message,args);

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setAuthor(user.getAsTag(), null,user.getEffectiveAvatarUrl());
        embedBuilder.setDescription("**" + ":no_entry: Ban rückgängig gemacht" + "**");
        embedBuilder.addField("Member",user.getAsMention() , true);
        embedBuilder.addField("Rückgängig gemacht von",sender.getAsMention(), true);
        embedBuilder.setFooter(sender.getUser().getAsTag(),sender.getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        Helper.sendEmbed(embedBuilder,message,false);


        //TODO delete the ban from the database

    }

    @Override
    public void removeSanction(Message message, String [] args) {

        User user = Helper.getUserFromCommandInput(message, args);

        Config.getInstance().getGuild().unban(user).queue();
    }


    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "`unban <user>`: Entfernt den Ban";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }

}
