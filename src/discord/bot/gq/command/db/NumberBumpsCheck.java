package discord.bot.gq.command.db;

import discord.bot.gq.entities.UserData;
import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class NumberBumpsCheck extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {


        String userMessageContent = event.getMessage().getContentRaw();
        String amountBumps = "SELECT number_bumps FROM user_bump WHERE id_discord = ?";
        String nextHigherUserAmountBumps = "SELECT id_discord, number_bumps FROM user_bump WHERE number_bumps > ? ORDER BY number_bumps, username LIMIT 1";
        String amountBumpsCommand = "hmb";
        String embedColor = "0x26b7b8";

        if (Helper.isValidCommand(userMessageContent, amountBumpsCommand)) {

            UserData userData = new UserData();

            Helper.getAmount(userData, amountBumps, nextHigherUserAmountBumps, event);

            Helper.sendAmount(userData, event, embedColor, "Nachrichten");
        }
    }
}