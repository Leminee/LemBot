package discord.bot.gq.command.db;

import discord.bot.gq.entities.UserData;
import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class NumberMessagesCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();

        String amountMessages = "SELECT number_message FROM user_message WHERE id_discord = ?";
        String nextHigherUserAmountMessages = "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1";
        String amountMessagesCommand = "hmm";
        String embedColor = "0xffffff";

        if (Helper.isValidCommand(userMessageContent, amountMessagesCommand)) {

            UserData userData = new UserData();

            Helper.getAmount(userData, amountMessages, nextHigherUserAmountMessages, event);

            Helper.sendAmount(userData, event, embedColor, "Nachrichten");

        }
    }
}
