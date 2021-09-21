package tech.goodquestion.lembot.command.moderation;

import tech.goodquestion.lembot.lib.Helper;
import tech.goodquestion.lembot.entities.Sanction;
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
        boolean hasMessageManagePermission = commandAuthor.hasPermission(Permission.MESSAGE_MANAGE);
        boolean hasManageRolesPermission = commandAuthor.hasPermission(Permission.MANAGE_ROLES);
        long channelId = event.getChannel().getIdLong();
        long reportSanctionChannelId = 871615666959032340L;

        if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "warn") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "mute") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban")) {

            if (!hasMessageManagePermission) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Permission Denied";
                Helper.createEmbed(embedError, "", embedDescription, Color.RED);
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

        }

        if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "warn") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "mute") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick") || sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban") && !sanctionCommand[1].isEmpty()) {

            if (!hasMessageManagePermission) {
                return;
            }

            if (sanctionCommand.length < 2) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Bitte gebe die ID des zu kickenden Users und den Grund für die Bestrafung an!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

            if (sanctionCommand.length < 3) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Bitte gebe einen Grund an!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }


            if (channelId != reportSanctionChannelId) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Dieser Befehl kann in diesem Channel nicht ausgeführt werden!";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
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

                    if (!hasManageRolesPermission) {

                        EmbedBuilder embedError = new EmbedBuilder();
                        String embedDescription = "Permission Denied";
                        Helper.createEmbed(embedError, "", embedDescription, Color.RED);
                        event.getChannel().sendMessage(embedError.build()).queue();
                        return;
                    }

                    EmbedBuilder embedError = new EmbedBuilder();
                    String embedDescription = "User ist nicht auf dem Server!";
                    Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
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


            StringBuilder sanctionReason = new StringBuilder();

            for (int i = 2; i < sanctionCommand.length; i++) {

                sanctionReason.append(sanctionCommand[i]).append(" ");
            }

            Sanction sanction = new Sanction();

            sanction.userId = member.getIdLong();
            sanction.userTag = member.getUser().getAsTag();
            sanction.userName = member.getUser().getName();
            sanction.author = commandAuthor.getUser().getAsTag();
            sanction.reason = sanctionReason.toString();
            sanction.channelName = event.getChannel().getName();

            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "warn")) {


                Role warnRole = event.getGuild().getRoleById(879448018372395048L);

                assert warnRole != null;
                member.getGuild().addRoleToMember(sanction.userId, warnRole).queue();

                Helper.sendDM(member.getUser(), "verwarnt", sanctionReason, sanctionedUserAsMention);

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention() +
                        "**" + " verwarnt." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                String warnedUserData = "INSERT INTO warned_user (id_warned_user,id_discord,user_tag, username, warn_author, warn_reason, channel_name) " +
                        "VALUES (NULL,?,?,?,?,?,?)";


                Helper.insertSanctionedUserData(warnedUserData, sanction);
                return;
            }


            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "mute")) {


                Role muteRole = event.getGuild().getRoleById(879329567947489352L);
                List<Role> userRoleList = Objects.requireNonNull(member).getRoles();
                String mutedUserData = "INSERT INTO muted_user (id_muted_user,id_discord,user_tag, username, mute_author, mute_reason, channel_name) " +
                        "VALUES (NULL,?,?,?,?,?,?)";


                for (Role role : userRoleList) {

                    event.getGuild().removeRoleFromMember(sanction.userId, role).queue();
                }


                event.getGuild().addRoleToMember(sanction.userId, Objects.requireNonNull(muteRole)).queue();

                Helper.sendDM(member.getUser(), "gemutet", sanctionReason, sanctionedUserAsMention);

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention()
                        + "**" + " gemutet." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData(mutedUserData, sanction);
                return;
            }

            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "kick")) {

                if (!hasManageRolesPermission) {
                    EmbedBuilder embedError = new EmbedBuilder();
                    String embedDescription = "Permission Denied";
                    Helper.createEmbed(embedError, "", embedDescription, Color.RED);
                    event.getChannel().sendMessage(embedError.build()).queue();
                    return;
                }

                String kickedUserDate = "INSERT INTO kicked_user (id_kicked_user,id_discord,user_tag, username, kick_author, kick_reason, channel_name) " +
                        "VALUES (NULL,?,?,?,?,?,?)";

                Helper.sendDM(member.getUser(), "gekickt", sanctionReason, sanctionedUserAsMention);

                member.kick(sanctionReason.toString()).complete();


                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention()
                        + "**" + " gekickt." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData(kickedUserDate, sanction);
                return;
            }


            if (sanctionCommand[0].equalsIgnoreCase(Helper.PREFIX + "ban")) {

                if (!hasManageRolesPermission) {
                    EmbedBuilder embedError = new EmbedBuilder();
                    String embedDescription = "Permission Denied";
                    Helper.createEmbed(embedError, "", embedDescription, Color.RED);
                    event.getChannel().sendMessage(embedError.build()).queue();
                    return;
                }

                String bannedUserData = "INSERT INTO banned_user (id_banned_user,id_discord,user_tag, username, ban_author, ban_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)";

                Helper.sendDM(member.getUser(), "gebannt", sanctionReason, sanctionedUserAsMention);

                member.ban(0, sanctionReason.toString()).complete();


                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + sanctionedUserAsMention + " wurde durch " + commandAuthor.getAsMention() + "**" + " gebannt." + "**" + "\n Angegebener Grund: " + sanctionReason);
                event.getChannel().sendMessage(confirmation.build()).queue();

                Helper.insertSanctionedUserData(bannedUserData, sanction);

            }
        }
    }
}
