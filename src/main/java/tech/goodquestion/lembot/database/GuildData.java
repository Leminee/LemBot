package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildData extends ListenerAdapter {

    public void onGuildUpdateIcon(@Nonnull GuildUpdateIconEvent event) {}

    public void onGuildUpdateName(@Nonnull GuildUpdateNameEvent event){}


    public void onGuildUpdateBanner(@Nonnull GuildUpdateBannerEvent event) {}


    public void onGuildUpdateBoostCount(@Nonnull GuildUpdateBoostCountEvent event) {

    }

    public void onGuildInviteCreate(@Nonnull GuildInviteCreateEvent event) {

        event.getInvite().getUses();


    }

    public void onGuildInviteDelete(@Nonnull GuildInviteDeleteEvent event) {

    }
}
