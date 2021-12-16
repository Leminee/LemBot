package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.database.CommandsHelper;

public class UpdatingUsername extends ListenerAdapter {

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {

        long userId = event.getMember().getIdLong();
        String oldUsername = event.getOldNickname();
        String newUsername = event.getNewNickname();
        String userTag = event.getMember().getUser().getAsTag();

        if (oldUsername == null) {
            oldUsername = event.getMember().getUser().getName();
        }

        if (newUsername == null) {
            newUsername = event.getMember().getUser().getName();
        }

        CommandsHelper.logUpdatedUsername(userId, userTag, oldUsername, newUsername);
        CommandsHelper.adjustUsername(CommandsHelper.ADJUSTING_NEW_USERNAME_IN_BUMPER,newUsername,userId);
        CommandsHelper.adjustUsername(CommandsHelper.ADJUSTING_NEW_USERNAME_IN_MESSAGE,newUsername,userId);

    }
}
