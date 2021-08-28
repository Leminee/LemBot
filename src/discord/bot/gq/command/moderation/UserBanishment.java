package discord.bot.gq.command.moderation;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class UserBanishment extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] sanctionCommand = event.getMessage().getContentRaw().split("\\s+");
        Member commandAuthor = event.getMessage().getMember();
        assert commandAuthor != null;
        boolean hasPermission = commandAuthor.hasPermission(Permission.MESSAGE_MANAGE);

        if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "warn") ||sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "mute") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban")) {


            if (!hasPermission) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Permission Denied";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordap819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

        }

        if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "warn") ||sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "mute") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban") && !sanctionCommand[1].isEmpty()) {

            if (!hasPermission) {
                return;
            }

            if (sanctionCommand.length < 2) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Bitte gebe die ID des zu kickenden Users und den Grund für die Bestrafung an!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordapp.com/attacents/819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

            if (sanctionCommand.length < 3) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Bitte gebe einen Grund an!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordapp.com/attacents/819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
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
                    Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discorda.com/attachments/819694809765380146/87Bildschirmfoto_2021-08-23_um_07.06.46.png");
                    event.getChannel().sendMessage(embedError.build()).queue();
                    return;

                }
            }

            assert member != null;
            if (member.hasPermission(Permission.ADMINISTRATOR)) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Admins/Moderatoren können nicht gekickt oder gebannt werden!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discoapp.com/attachments/819694809765380146/879230207763038228/Bildschirmfoto_2021-086.png");
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

            String sanctionedUserAsMention = member.getAsMention();
            String sanctionedUserAsTag = member.getUser().getAsTag();
            String sanctionedUserName = member.getUser().getName();

            StringBuilder sanctionReason = new StringBuilder();

            for (int i = 2; i < sanctionCommand.length; i++) {

                sanctionReason.append(sanctionCommand[i]).append(" ");
            }

            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "warn")) {

                String sanctionedUserId = member.getId();

                Role warnRole = event.getGuild().getRoleById(879448018372395048L);

                assert warnRole != null;
                member.getGuild().addRoleToMember(sanctionedUserId, warnRole).queue();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention() + "**" + " verwarnt." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData("INSERT INTO warned_user (id_warned_user,id_discord,user_tag, username, warn_author, warn_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)", member.getIdLong(), sanctionedUserAsTag, sanctionedUserName, commandAuthor.getUser().getAsTag(), sanctionReason.toString(), event.getChannel().getName());
                return;
            }


            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "mute")) {

                String sanctionedUserId = member.getId();

                Role muteRole = event.getGuild().getRoleById(879329567947489352L);

                List<Role> userRoleList = Objects.requireNonNull(member).getRoles();


                for (Role role : userRoleList) {

                    event.getGuild().removeRoleFromMember(sanctionedUserId, role).queue();
                }

                event.getGuild().addRoleToMember(sanctionedUserId, Objects.requireNonNull(muteRole)).queue();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention() + "**" + " gemutet." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData("INSERT INTO muted_user (id_muted_user,id_discord,user_tag, username, mute_author, mute_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)", member.getIdLong(), sanctionedUserAsTag, sanctionedUserName, commandAuthor.getUser().getAsTag(), sanctionReason.toString(), event.getChannel().getName());
                return;
            }

            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick")) {

                if (!commandAuthor.hasPermission(Permission.KICK_MEMBERS)) {
                    EmbedBuilder embedError = new EmbedBuilder();
                    String embedDescription = "Permission Denied";
                    Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordap819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                    event.getChannel().sendMessage(embedError.build()).queue();
                    return;
                }


                member.kick(sanctionReason.toString()).complete();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention() + "**" + " gekickt." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData("INSERT INTO kicked_user (id_kicked_user,id_discord,user_tag, username, kick_author, kick_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)", member.getIdLong(), sanctionedUserAsTag, sanctionedUserName, commandAuthor.getUser().getAsTag(), sanctionReason.toString(), event.getChannel().getName());
                return;
            }


            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban")) {

                if (!commandAuthor.hasPermission(Permission.BAN_MEMBERS)) {
                    EmbedBuilder embedError = new EmbedBuilder();
                    String embedDescription = "Permission Denied";
                    Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED, "https://cdn.discordap819694809765380146/879230207763038228/Bildschirmfoto_2021-08-23_um_07.06.46.png");
                    event.getChannel().sendMessage(embedError.build()).queue();
                    return;
                }

                member.ban(7, sanctionReason.toString()).complete();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention() + "**" + " gebannt." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData("INSERT INTO banned_user (id_banned_user,id_discord,user_tag, username, ban_author, ban_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)", member.getIdLong(), sanctionedUserAsTag, sanctionedUserName, commandAuthor.getUser().getAsTag(), sanctionReason.toString(), event.getChannel().getName());

            }
        }
    }
}
