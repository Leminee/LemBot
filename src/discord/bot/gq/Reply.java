package discord.bot.gq;

import discord.bot.gq.db.ConnectionToDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Objects;

public class Reply extends ListenerAdapter {

   @Override
   public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
       String userMessage = event.getMessage().getContentRaw();
       String userName = Objects.requireNonNull(event.getMember()).getAsMention();
       Date date = new Date();

       if (userMessage.equalsIgnoreCase("hallo")) {
           if (!event.getMember().getUser().isBot()) {
               event.getChannel().sendTyping().queue();
               event.getChannel().sendMessage("Hi, Wie geht's dir? " + userName + " Was hast du heute schon gemacht? und was wirst du heute noch tun?").queue();
           }
       }

       if (userMessage.equalsIgnoreCase("Wie spät ist es?") || userMessage.equalsIgnoreCase("Wie spät?") ||
               userMessage.equalsIgnoreCase("Uhrzeit?") || userMessage.equalsIgnoreCase("Welche Uhrzeit?") ||
               userMessage.equalsIgnoreCase(BotMain.prefix + "time")) {
           if (!event.getMember().getUser().isBot()) {
               event.getChannel().sendTyping().queue();
               event.getChannel().sendMessage("CurrentTime: " + date.toString().substring(11, 16)).queue();
           }
       }

       if (userMessage.equalsIgnoreCase(BotMain.prefix + "ping") || userMessage.equalsIgnoreCase("!ping") || userMessage.equalsIgnoreCase("ping")) {
           if (!event.getMember().getUser().isBot()) {
               event.getChannel().sendTyping().queue();
               event.getChannel().sendMessage("pong").queue();

           }

       }

       if (userMessage.equalsIgnoreCase(BotMain.prefix + "help") || userMessage.equalsIgnoreCase(BotMain.prefix + "hilfe")) {
           if (!event.getMember().getUser().isBot()) {

               EmbedBuilder embedBuilder = new EmbedBuilder();
               embedBuilder.setTitle("HILFE \n");
               embedBuilder.setColor(0x002d47);
               embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
               embedBuilder.setDescription("Hallo, ich heiße **GQ** und bin ein Bot für GoodQuestion:) \n" + "\n" +
                       "                -------------------- **BEFEHLSLISTE** -------------------- \n  " +
                       "\n" + " Prefix:  `?`" + " \n" + " \n" +
                       "`?help`: Nachricht mit Befehlsliste \n  " +
                       "`?ping`: pong -  wenn Bot online ist" + " \n" +
                       "`?clear - [zahl]`: löscht Nachrichten" + " \n" +
                       "`?kick [@username]`: kickt den User" + "\n" +
                       "`?top`: Liste der Top 3 User mit den meisten Nachrichten" + "\n" +
                       "`?topb`: Liste der Top 3 Server-Bumper " + "\n" +
                       "`?topu`: Liste der 3 am häufigsten gepingten User" + "\n" +
                       "`?topc`: Liste der 3 aktivisten Channels" + "\n" +
                       "`?topf`: Liste der 3 befreundesten User" + "\n" +
                       "`?tope`: Liste der 3 am häufigsten benutzten Emojis auf GQ" + "\n" +
                       "`?time`: Die aktuelle Uhrzeit" + "\n");

               event.getChannel().sendMessage(embedBuilder.build()).queue();
           }
       }
       if ((userMessage.startsWith("kann sich wer") || (userMessage.contains("ann sich wer") || userMessage.startsWith("kennt sich jemand") || userMessage.startsWith("Kennt sich jemand") ||
               userMessage.startsWith("kennt sich wer aus mit") || userMessage.contains("kann jemand helfen bei")) && userMessage.length() < 35)) {
           if (!event.getMember().getUser().isBot()) {
               event.getChannel().sendMessage("Stelle einfach deine Frage! " + userName).queue();

           }
       }

       if (userMessage.equalsIgnoreCase(BotMain.prefix + "top")) {

           try {

               ConnectionToDB db = new ConnectionToDB();
               db.initialize();

               String selectTopUser = "SELECT username, number_message FROM user_message ORDER BY number_message DESC LIMIT 3;";
               Statement statement = db.connection.createStatement();
               ResultSet rS = statement.executeQuery(selectTopUser);

               EmbedBuilder embedBuilder = new EmbedBuilder();
               embedBuilder.setTitle("Liste der TOP 3 User");
               embedBuilder.setDescription("");
               embedBuilder.setColor(Color.white);

               int top = 1;

               while (rS.next()) {

                   embedBuilder.addField("TOP " + top, rS.getString(1).toUpperCase(), false);
                   embedBuilder.addField("Anzahl Nachrichten", rS.getString(2), false);
                   top++;

               }

               event.getChannel().sendMessage(embedBuilder.build()).queue();

               rS.close();
               statement.close();


           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
       }

       if (userMessage.equalsIgnoreCase(BotMain.prefix + "topb")) {

           try {

               ConnectionToDB db = new ConnectionToDB();
               db.initialize();

               String selectTopBumper = "SELECT username, number_bumps FROM user_bump ORDER BY number_bumps DESC LIMIT 3;";
               Statement statement = db.connection.createStatement();
               ResultSet rS = statement.executeQuery(selectTopBumper);

               EmbedBuilder embedBuilder = new EmbedBuilder();
               embedBuilder.setTitle("TOP 3 User mit den meisten Bumps");
               embedBuilder.setDescription("");
               embedBuilder.setColor(0x26b7b8);
               embedBuilder.setThumbnail("https://plane-dein-training.de/assets/media/dis.png");

               int top = 1;

                    while (rS.next()) {

                        embedBuilder.addField("TOP " + top,  rS.getString(1), false);
                        top++;

                    }

               event.getChannel().sendMessage(embedBuilder.build()).queue();

               rS.close();
               statement.close();


           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }


           }
       }
   }
