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
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] kickCommand = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();

        if (kickCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") && !kickCommand[1].isEmpty()) {

            assert authorCommand != null;
            if (!authorCommand.hasPermission(Permission.MESSAGE_MANAGE)) {

                try {
                    throw new Exception("Permission Denied");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (kickCommand.length < 3) {
                event.getChannel().sendMessage(getHelpEmbed("Hilfe", "Richtige Command " + "-> " + Helper.PREFIX + "kick [User ID] [Grund des Kicks] ")).complete();
                return;
            }

            try {

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
                }

                if(member.hasPermission(Permission.ADMINISTRATOR)) {

                    throw new Exception("Permission Denied");
                }

                String kickedUser = member.getAsMention();
                String kickUserTag = member.getUser().getAsTag();
                String kickUserName = member.getUser().getName();

                /*member.kick(kickCommand[2]).complete();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + "*" + kickedUser + "*" + " wurde durch " + authorCommand.getAsMention() + " gekickt." + "\n Angegebener Grund: " + "*" + kickCommand[2] + "*");
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertKickedUserData(member.getIdLong(),kickUserTag,kickUserName,authorCommand.getUser().getAsTag(), kickCommand[2],event.getChannel().getName());
                */

                event.getChannel().sendMessage("Command funktioniert nicht! " + authorCommand).queue();

            } catch (Exception e) {
                e.getStackTrace();

                event.getChannel().sendMessage(getErrorEmbed("Fehler", "Ein unerwarteter Fehler ist aufgetreten!")).complete();
            }
        }
    }


    public MessageEmbed getHelpEmbed(String title, String description) {
        EmbedBuilder howToUse = new EmbedBuilder();
        howToUse.setColor(0x8EE5EE);
        howToUse.setTitle(title);
        if (description != null && !description.equals(""))
            howToUse.setDescription(description);

        return howToUse.build();
    }

    public MessageEmbed getErrorEmbed(String title, String description) {
        EmbedBuilder error = new EmbedBuilder();
        error.setColor(0xff0000);
        error.setTitle(title);
        if (description != null && !description.equals(""))
            error.setDescription(description);

        return error.build();
    }

}