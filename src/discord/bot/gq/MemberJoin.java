package discord.bot.gq;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;


public class MemberJoin extends ListenerAdapter {


    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        String[] messages = {"""
Hallo [member], Willkommen auf **GoodQuestion (GQ)**!

Stelle Dich bitte in diesem Channel kurz vor, damit ein Moderator dir die entsprechenden Rollen zuweist!
(Beispiel: Ich bin Lem und lerne momentan Java und SQL.)"""};

        TextChannel channel = event.getGuild().getTextChannelById("779107500381175808");
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();

        String output = messages[0].replace("[member]", event.getMember().getAsMention());

        Objects.requireNonNull(channel).sendMessage(output + "\n" + avatarUrl).queue();

    }
}