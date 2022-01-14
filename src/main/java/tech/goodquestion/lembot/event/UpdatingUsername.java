package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.database.CommandHelper;

public class UpdatingUsername extends ListenerAdapter {

    @Override
    public void onGuildMemberUpdateNickname(final GuildMemberUpdateNicknameEvent event) {

        final long userId = event.getMember().getIdLong();
        final String userTag = event.getMember().getUser().getAsTag();

        String oldUsername = event.getOldNickname();
        String newUsername = event.getNewNickname();

        if (oldUsername == null) {
            oldUsername = event.getMember().getUser().getName();
        }

        if (newUsername == null) {
            newUsername = event.getMember().getUser().getName();
        }

        CommandHelper.logUpdatedUsername(userId, userTag, oldUsername, newUsername);
        CommandHelper.adjustUsername(CommandHelper.ADJUSTING_NEW_USERNAME_IN_BUMPER,newUsername,userId);
        CommandHelper.adjustUsername(CommandHelper.ADJUSTING_NEW_USERNAME_IN_MESSAGE,newUsername,userId);

    }
}