package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ActiveUsers extends ListenerAdapter {

    public void onUserUpdateOnlineStatus(@Nonnull UserUpdateOnlineStatusEvent event) {

        OnlineStatus newStatus = event.getNewOnlineStatus();
        String userTag = event.getMember().getUser().getAsTag();
        long userId = event.getUser().getIdLong();

        CommandsHelper.logMemberStatusChange(userId, userTag, newStatus);

        int approximatePresentMember = event.getGuild().retrieveMetaData().complete().getApproximatePresences();

        if (QueryHelper.isActiveUserRecord(approximatePresentMember)) {

            EmbedBuilder embedBuilder = new EmbedBuilder();

            Helper.createEmbed(embedBuilder,"New Active User Record", "Der neue Record an gleichzeitig aktiven Usern liegt bei: " + "**" + approximatePresentMember + "** :tada:  ", EmbedColorHelper.RECORD);
            Objects.requireNonNull(event.getJDA().getTextChannelById(Config.getInstance().getChannel().getBumpChannel().getIdLong())).sendMessage(embedBuilder.build()).queue();

        }

        CommandsHelper.logActiveMemberCount(approximatePresentMember);
    }
}
