package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class NumberMessagesCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        try {

            Helper.sendAmount("SELECT number_message FROM user_message WHERE id_discord = ?",
                    "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1",
                    "hmm",
                    event,
                    "0x0E0E42",
                    "Nachrichten");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
