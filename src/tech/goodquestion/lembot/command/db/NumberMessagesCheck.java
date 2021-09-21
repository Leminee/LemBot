package tech.goodquestion.lembot.command.db;

import tech.goodquestion.lembot.entities.UserData;
import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class NumberMessagesCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String amountMessagesCommand = "hmm";


        if (Helper.isValidCommand(userMessageContent, amountMessagesCommand)) {

            UserData userData = new UserData();

            String amountMessages = "SELECT number_message FROM user_message WHERE id_discord = ?";
            String nextHigherUserAmountMessages = "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1";
            String embedColor = "0xffffff";

            Helper.getAmount(userData, amountMessages, nextHigherUserAmountMessages, event);

            Helper.sendAmount(userData, event, embedColor, "Nachrichten");

        }
    }
}
