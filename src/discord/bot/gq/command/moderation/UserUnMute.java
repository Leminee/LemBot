package discord.bot.gq.command.moderation;

import discord.bot.gq.database.ConnectionToDB;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserUnMute extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {


        String[] unlockCommand = event.getMessage().getContentRaw().split("\\s+");
        Member commandAuthor = event.getMessage().getMember();

        if (unlockCommand[0].equalsIgnoreCase(Helper.PREFIX + "unmute")) {


            assert commandAuthor != null;
            if (!commandAuthor.hasPermission(Permission.MESSAGE_MANAGE)) {

                EmbedBuilder embedError = new EmbedBuilder();
                String embedDescription = "Permission Denied";
                Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
                event.getChannel().sendMessage(embedError.build()).queue();
                return;
            }

            if (unlockCommand.length < 2) {
                return;
            }

            if (unlockCommand[0].equalsIgnoreCase(Helper.PREFIX + "unmute") && !unlockCommand[1].isEmpty()) {


                List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
                Member member = mentionedMembers.size() > 0 ? mentionedMembers.get(0) : null;

                if (member == null) {

                    try {

                        User mentionedUser = event.getJDA().retrieveUserById(unlockCommand[1], true).complete();

                        if (mentionedUser != null) {
                            member = event.getGuild().retrieveMember(mentionedUser).complete();
                        }

                    } catch (ErrorResponseException errorResponseException) {

                        EmbedBuilder embedError = new EmbedBuilder();
                        String embedDescription = "User ist nicht auf dem Server!";
                        Helper.createEmbed(embedError, "Fehler", embedDescription, Color.RED);
                        event.getChannel().sendMessage(embedError.build()).queue();
                        return;
                    }
                }


                assert member != null;

                String unmuteUserAsMention = member.getAsMention();
                long unmuteUserId = member.getIdLong();
                Role mutedRole = event.getGuild().getRoleById(879329567947489352L);

                assert mutedRole != null;
                member.getGuild().removeRoleFromMember(unmuteUserId, mutedRole).queue();

                EmbedBuilder confirmation = new EmbedBuilder();
                confirmation.setColor(0x00ff60);
                confirmation.setTitle("Bestätigung");
                confirmation.setDescription("User " + unmuteUserAsMention + " wurde durch " + commandAuthor.getAsMention() + " erfolgreich **" + " ungemutet." + "**");
                event.getChannel().sendMessage(confirmation.build()).queue();


                ConnectionToDB connectionToDB = new ConnectionToDB();
                connectionToDB.initialize();


                long userId = member.getIdLong();
                String enableUser = "0";

                String userMuted = "SELECT activ FROM muted_user WHERE id_discord = ?;";

                try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(userMuted)) {


                    preparedStatement.setLong(1, userId);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {


                        String userUnmute = "UPDATE muted_user SET activ = ? WHERE id_discord = ? ORDER BY muted_on DESC LIMIT 1;";

                        PreparedStatement preparedStatementOne = connectionToDB.getConnection().prepareStatement(userUnmute);

                        preparedStatementOne.setString(1, enableUser);
                        preparedStatementOne.setLong(2, userId);

                        preparedStatementOne.executeUpdate();

                    }

                } catch (SQLException sqlException) {
                    System.out.println(sqlException.getMessage());
                }

            }
        }
    }
}