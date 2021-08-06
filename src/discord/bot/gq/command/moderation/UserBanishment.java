package discord.bot.gq.command.moderation;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserBanishment extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived (@NotNull GuildMessageReceivedEvent event) {

        String[] kickCommand = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();

        if (kickCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") && !kickCommand[1].isEmpty()) {

            if (kickCommand.length < 3) {
                event.getChannel().sendMessage(getHelpEmbed("Hilfe", "Richtige Command " + "-> " + Helper.PREFIX + "kick [User ID] [Grund des Kicks] ")).complete();
                return;
            }
            try {
                assert authorCommand != null;
                if (!authorCommand.hasPermission(Permission.MESSAGE_MANAGE)) {
                    //event.getChannel().sendMessage(getErrorEmbed("Permission Denied")).complete();
                    throw new Exception("Permission Denied");
                }


                List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
                Member member = mentionedMembers.size() > 0 ? mentionedMembers.get(0) : null;

                if (member == null) {
                    User mentionedUser = event.getJDA().retrieveUserById(kickCommand[1], true).complete();

                    if (mentionedUser != null) {
                        member = event.getGuild().retrieveMember(mentionedUser).complete();
                    }
                }

                if (member == null) {
                    throw new Exception("User ist nicht auf dem Server");
                    //event.getChannel().sendMessage(getErrorEmbed("Error", "User ist nicht auf dem Server")).complete();
                    //return;
                }

                String kickedUser = member.getAsMention();

                member.kick(kickCommand[2]).complete();

                event.getChannel().sendMessage(getConfirmationEmbed("BestÃ¤tigung", "User " + "*" + kickedUser + "* " + " wurde durch " + authorCommand.getAsMention() + " gekickt." + "\n Angegebener Grund: " + kickCommand[2])).complete();
                return;
            } catch (Exception e) {
                e.getStackTrace(); //oder so
                event.getChannel().sendMessage(getErrorEmbed("Error", e.getMessage())).complete();   //ersetz getMessage, hab kein IntelliJ grad
            }
        }
    }


        //In andere Datei! Utils/Embeds.java oder so
        public MessageEmbed getHelpEmbed (String title, String description){
            EmbedBuilder howToUse = new EmbedBuilder();
            howToUse.setColor(0x00ffff);
            howToUse.setTitle(title); //"Hilfe"
            if (description != null && description != "")
                howToUse.setDescription(description); //"Richtige Command " + "-> " + Helper.PREFIX + "kick [User ID] [Grund des Kicks] "

            return howToUse.build();
        }

        public MessageEmbed getErrorEmbed (String title, String description){
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff0000);
            error.setTitle(title);
            if (description != null && description != "")
                error.setDescription(description);

            return error.build();
        }

        public MessageEmbed getConfirmationEmbed(String title, String description){
            EmbedBuilder confirmation = new EmbedBuilder();
            confirmation.setColor(0x00ff60);
            confirmation.setTitle(title);
            if (description != null && description != "")
                confirmation.setDescription(description);

            return confirmation.build();
        }

        /*catch (Exception e) {
        System.out.println(e.getStackTrace()); //oder so
        String errorMsg = e.getMessage();

        if(e instanceof NullPointerException) {
            errorMsg = "Dies ist eine custom Null Pointer Exception!";
        }
        event.getChannel().sendMessage(getErrorEmbed("Error", errorMsg)).complete();
    }*/

}