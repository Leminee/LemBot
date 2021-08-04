package discord.bot.gq.command.db;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

    public class TopPingedUser extends ListenerAdapter {

        @Override
        public void onGuildMessageReceived (@NotNull GuildMessageReceivedEvent event){
            Helper.selectTop(
                    "topu", event,
                    "SELECT content, COUNT(id_discord) FROM `user_message_content` WHERE content LIKE '<@%' GROUP BY content HAVING COUNT(id_discord) > 1 ORDER BY COUNT(id_discord) DESC LIMIT 3",
                    "Die leute die am meisten Gepingt wurden");
        }
    }

