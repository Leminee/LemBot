package discord.bot.gq;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class PingMe extends ListenerAdapter{

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();

        if (userMessage.equalsIgnoreCase("!d bump")) {

            Timer timer = new Timer();

               timer.schedule(new TimerTask() {
                   @Override
                   public void run() {
                       if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {
                           event.getChannel().sendMessage("Jetzt kann wieder gebumpt werden <@739143338975952959>").queue();
                       }
                   }
               }, 7200000);
        }
    }



}





