package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ProfileUpdating extends ListenerAdapter {

    @Override
    public void onGuildMemberUpdateNickname(final GuildMemberUpdateNicknameEvent event) {

        final long memberId = event.getMember().getIdLong();
        final String memberTag = event.getMember().getUser().getAsTag();

        String oldNickName = event.getOldNickname() == null ? event.getMember().getUser().getName() : event.getOldNickname();
        String newNickname = event.getNewNickname() == null ? event.getMember().getUser().getName() : event.getNewNickname();

        CommandHelper.logUpdatedNickname(memberId, memberTag, oldNickName, newNickname);
        CommandHelper.adjustNickname(CommandHelper.ADJUSTING_NICKNAME_IN_BUMPER, newNickname, memberId);
        CommandHelper.adjustNickname(CommandHelper.ADJUSTING_NICKNAME_IN_MESSAGE, newNickname, memberId);
    }

    @Override
    public void onUserUpdateName(final UserUpdateNameEvent event) {

        final long userId = event.getUser().getIdLong();
        final String userTag = event.getUser().getAsTag();
        final String oldUsername = event.getOldName();
        final String newUsername = event.getNewName();

        CommandHelper.logUpdatedUsername(userId, userTag, oldUsername, newUsername);
    }

    @Override
    public void onUserUpdateDiscriminator(final UserUpdateDiscriminatorEvent event) {

        final long userId = event.getUser().getIdLong();
        final String userTag = event.getUser().getAsTag();
        final String oldUsername = event.getOldDiscriminator();
        final String newUsername = event.getNewDiscriminator();

        CommandHelper.logUpdatedUsername(userId, userTag, oldUsername, newUsername);
    }
}