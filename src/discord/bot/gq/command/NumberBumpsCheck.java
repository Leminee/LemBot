package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class NumberBumpsCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        try {

            Helper.sendAmount("SELECT number_bumps FROM user_bump WHERE id_discord = ?",
                    "SELECT id_discord, number_bumps FROM user_bump WHERE number_bumps > ? ORDER BY number_bumps, username LIMIT 1",
                    "hmb",
                    event,
                    "0x26b7b8",
                    "Bumps");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
