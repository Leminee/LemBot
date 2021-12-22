package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class BoostStorage extends ListenerAdapter {

    @Override
    public void onGuildUpdateBoostTier(@Nonnull final GuildUpdateBoostTierEvent event) {

    }
    @Override
    public void onGuildUpdateBoostCount(@Nonnull final GuildUpdateBoostCountEvent event) {

    }
    @Override
    public void onGuildMemberUpdateBoostTime(@Nonnull final GuildMemberUpdateBoostTimeEvent event) {

    }
}
