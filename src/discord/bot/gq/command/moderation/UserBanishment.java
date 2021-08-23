package discord.bot.gq.command.moderation;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class UserBanishment extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] sanctionCommand = event.getMessage().getContentRaw().split("\\s+");
        Member commandAuthor = event.getMessage().getMember();

        if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban")) {


            assert commandAuthor != null;
            if (!commandAuthor.hasPermission(Permission.MESSAGE_MANAGE)) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Permission Denied";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordapp.com/attachments/819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

        }


        if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban") && !sanctionCommand[1].isEmpty()) {

            assert commandAuthor != null;
            if (!commandAuthor.hasPermission(Permission.MESSAGE_MANAGE)) {
                return;
            }

            if (sanctionCommand.length < 3) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Bitte gebe einen Grund an!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordapp.com/attachments/819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }


            List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
            Member member = mentionedMembers.size() > 0 ? mentionedMembers.get(0) : null;

            if (member == null) {

                try {

                    User mentionedUser = event.getJDA().retrieveUserById(sanctionCommand[1], true).complete();


                    if (mentionedUser != null) {
                        member = event.getGuild().retrieveMember(mentionedUser).complete();
                    }
                } catch (ErrorResponseException errorResponseException) {

                    EmbedBuilder embedError = new EmbedBuilder();
                    String embedDescription = "User ist nicht auf dem Server!";
                    Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordapp.com/attachments/819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                    event.getChannel().sendMessage(embedError.build()).queue();
                    return;

                }
            }

            assert member != null;
            if (member.hasPermission(Permission.ADMINISTRATOR)) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Admins/Moderatoren können nicht gekickt werden!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordapp.com/attachments/819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

            String sanctionedUserAsMention = member.getAsMention();
            String sanctionedUserAsTag = member.getUser().getAsTag();
            String sanctionedUserName = member.getUser().getName();

            String sanctionReason = "";

            for (int i = 2; i < sanctionCommand.length; i++) {

                sanctionReason =  sanctionReason + sanctionCommand[i] + " ";
            }


            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick")) {


                member.kick(sanctionReason).complete();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + "*" + sanctionedUserAsMention + "*" + " wurde durch " + commandAuthor.getAsMention() + "**" + " gekickt." + "**" + "\n Angegebener Grund: " + sanctionReason );
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData("INSERT INTO kicked_user (id_kicked_user,id_discord,user_tag, username, kick_author, kick_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)",member.getIdLong(), sanctionedUserAsTag, sanctionedUserName, commandAuthor.getUser().getAsTag(), sanctionCommand[2], event.getChannel().getName());
                return;
            }


            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban")) {

                member.ban(7, sanctionReason).complete();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + "*" + sanctionedUserAsMention + "*" + " wurde durch " + commandAuthor.getAsMention() + "**" + " gebannt." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData("INSERT INTO banned_user (id_banned_user,id_discord,user_tag, username, ban_author, ban_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)",member.getIdLong(), sanctionedUserAsTag, sanctionedUserName, commandAuthor.getUser().getAsTag(), sanctionCommand[2], event.getChannel().getName());

            }
        }
    }
}
