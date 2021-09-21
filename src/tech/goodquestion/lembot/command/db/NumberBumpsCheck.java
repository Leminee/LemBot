package tech.goodquestion.lembot.command.db;

import tech.goodquestion.lembot.entities.UserData;
import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class NumberBumpsCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {


        String userMessageContent = event.getMessage().getContentRaw();
        String amountBumpsCommand = "hmb";

        if (Helper.isValidCommand(userMessageContent, amountBumpsCommand)) {

            UserData userData = new UserData();

            String amountBumps = "SELECT number_bumps FROM user_bump WHERE id_discord = ?";
            String nextHigherUserAmountBumps = "SELECT id_discord, number_bumps FROM user_bump WHERE number_bumps > ? ORDER BY number_bumps, username LIMIT 1";
            String embedColor = "0x26b7b8";

            Helper.getAmount(userData, amountBumps, nextHigherUserAmountBumps, event);

            Helper.sendAmount(userData, event, embedColor, "Bumps");
        }
    }
}