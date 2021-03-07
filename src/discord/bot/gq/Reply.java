package discord.bot.gq;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Date;
import java.util.Objects;


public class Reply extends ListenerAdapter {

   @Override
   public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String userMessage = event.getMessage().getContentRaw();
        String userName = Objects.requireNonNull(event.getMember()).getAsMention();
        Date date = new Date();


        if (userMessage.equalsIgnoreCase(BotMain.prefix + "hallo")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Hi, Wie geht's dir? " + userName + " Was hast du heute schon gemacht? und was wirst du heute noch tun?").queue();
            }
        }

       if (userMessage.equalsIgnoreCase("Wie spät ist es?") || userMessage.equalsIgnoreCase("Wie spät?") ||
               userMessage.equalsIgnoreCase("Uhrzeit?") || userMessage.equalsIgnoreCase("Welche Uhrzeit?")) {
           if (!event.getMember().getUser().isBot()) {
               event.getChannel().sendTyping().queue();
               event.getChannel().sendMessage("CurrentTime: " +date.toString().substring(11,16)).queue();
           }
       }

       if(userMessage.equalsIgnoreCase(BotMain.prefix + "ping") || userMessage.equalsIgnoreCase("!ping") ||userMessage.equalsIgnoreCase("ping")) {
           if (!event.getMember().getUser().isBot()) {
               event.getChannel().sendTyping().queue();
               event.getChannel().sendMessage("pong").queue();

           }

       }

       if(userMessage.equalsIgnoreCase(BotMain.prefix + "help") || userMessage.equalsIgnoreCase(BotMain.prefix + "hilfe")) {
           if (!event.getMember().getUser().isBot()) {
               //TODO--

           }
       }
       if((userMessage.startsWith("kann sich wer") ||(userMessage.startsWith("Kann sich wer") || userMessage.startsWith("kennt sich jemand") || userMessage.startsWith("Kennt sich jemand") || userMessage.startsWith("kennt sich wer aus mit")) && userMessage.length() < 40)) {
           if (!event.getMember().getUser().isBot()) {
               event.getChannel().sendMessage("Stelle einfach deine Frage! " + userName).queue();


           }
       }

       }
}
