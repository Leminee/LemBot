package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import javax.annotation.Nonnull;
import java.util.Objects;

public class OnlineMemberStorage extends ListenerAdapter {

    public void onUserUpdateOnlineStatus(@Nonnull final UserUpdateOnlineStatusEvent event) {

        final OnlineStatus newStatus = event.getNewOnlineStatus();
        final String userTag = event.getMember().getUser().getAsTag();
        final long userId = event.getUser().getIdLong();

        CommandHelper.logMemberStatusChange(userId, userTag, newStatus);

        final int approximatePresentMember = event.getGuild().retrieveMetaData().complete().getApproximatePresences();

        if (QueryHelper.isActiveUserRecord(approximatePresentMember)) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();

            Helper.createEmbed(embedBuilder,"Neuer Record", "Der neue Record an gleichzeitig aktiven Usern liegt bei: " + "**" + approximatePresentMember + "** :tada:  ", EmbedColorHelper.RECORD);
            Objects.requireNonNull(event.getJDA().getTextChannelById(Config.getInstance().getChannel().getBumpChannel().getIdLong())).sendMessageEmbeds(embedBuilder.build()).queue();

        }

        CommandHelper.logActiveMemberCount(approximatePresentMember);
    }
}
