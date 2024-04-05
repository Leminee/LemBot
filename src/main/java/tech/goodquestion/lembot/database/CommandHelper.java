package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.entity.InviteTrackingData;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.VoiceChannel;
import tech.goodquestion.lembot.library.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CommandHelper {

    private static final String MEMBER_STATUS = "INSERT INTO user_status (id_discord, user_tag, status) VALUES (?,?,?);";
    private static final String INSERT_MEMBER_AMOUNT = "INSERT INTO number_member (total_member) VALUES (?);";
    public static final String LEAVE_LOG = "INSERT INTO user_leave (id,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String JOIN_LOG = "INSERT INTO user_join (id,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String BAN_DATA = "INSERT INTO banned_user (id,id_discord,user_tag, username, ban_author, reason, channel_name) VALUES (NULL,?,?,?,?,?,?)";
    public static final String WARN_DATA = "INSERT INTO warned_user (id,id_discord,user_tag, username, warn_author, reason, channel_name) " + "VALUES (NULL,?,?,?,?,?,?)";
    public static final String ACTIVE_MEMBER_LOG = "INSERT INTO user_online (amount) VALUES (?);";
    public static final String ADJUSTING_NICKNAME_IN_BUMPER = "UPDATE user_bump SET username = ? WHERE id_discord = ?;";
    public static final String ADJUSTING_NICKNAME_IN_MESSAGE = "UPDATE user_message SET username = ? WHERE id_discord = ?;";
    public static final String NICKNAME_UPDATED_LOG = "INSERT INTO updated_nickname (id, id_discord, user_tag, old_nickname, new_nickname) VALUES (NULL,?,?,?,?);";
    public static final String USERNAME_UPDATED_LOG = "INSERT INTO updated_username (id, id_discord, user_tag, old_username, new_username) VALUES (NULL,?,?,?,?);";
    public static final String EXCEPTION_LOG = "INSERT INTO exception (id_exception, occurred_in, type, details) VALUES (NULL,?,?,?);";
    public static final String INVITE_TRACKING_LOG = "INSERT INTO invite_tracking (id, url, used_by, invited_by, amount) VALUES (NULL,?,?,?,?);";
    private static final String CLASS_NAME = CommandHelper.class.getName();
    private static final String ADVERTISING = "INSERT INTO advertising (id, id_discord, user_tag) VALUES (NULL, ?, ?)";
    private static final String MUTE_DATA = "INSERT INTO muted_user (id,id_discord, user_tag, username, mute_author, duration, reason, channel_name) " + "VALUES (NULL,?,?,?,?,?,?,?)";

    private CommandHelper() {

    }

    public static void adjustNickname(final String adjustingDateQuery, final String newUsername, final long userId) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(adjustingDateQuery)) {

            preparedStatement.setString(1, newUsername);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }
    }

    public static void logMemberStatusChange(final long userId, final String userTag, final OnlineStatus newStatus) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(MEMBER_STATUS)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setBlob(2, Helper.changeCharacterEncoding(preparedStatement, userTag));
            preparedStatement.setString(3, String.valueOf(newStatus));
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logUpdatedNickname(final long userId, final String userTag, final String oldUsername, final String newUsername) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(NICKNAME_UPDATED_LOG)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setBlob(2, Helper.changeCharacterEncoding(preparedStatement, userTag));
            preparedStatement.setBlob(3, Helper.changeCharacterEncoding(preparedStatement, oldUsername));
            preparedStatement.setBlob(4, Helper.changeCharacterEncoding(preparedStatement, newUsername));
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logUpdatedUsername(final long userId, final String userTag, final String oldUsername, final String newUsername) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(USERNAME_UPDATED_LOG)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setBlob(2, Helper.changeCharacterEncoding(preparedStatement, userTag));
            preparedStatement.setBlob(3, Helper.changeCharacterEncoding(preparedStatement, oldUsername));
            preparedStatement.setBlob(4, Helper.changeCharacterEncoding(preparedStatement, newUsername));
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logActiveMemberCount(final int memberCount) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ACTIVE_MEMBER_LOG)) {
            preparedStatement.setLong(1, memberCount);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logMemberAmount(final Guild guild) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MEMBER_AMOUNT)) {
            preparedStatement.setInt(1, guild.getMemberCount());
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logUserLeave(final User user) {
        logMemberStatus(LEAVE_LOG, user);
    }

    public static void logUserJoin(final User user) {
        logMemberStatus(JOIN_LOG, user);
    }

    private static void logSanction(final String query, final Sanction sanction) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, sanction.userId);
            preparedStatement.setString(2, sanction.userTag);
            preparedStatement.setString(3, sanction.userName);
            preparedStatement.setString(4, sanction.author);
            preparedStatement.setString(5, sanction.reason);
            preparedStatement.setString(6, sanction.channelName);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logMemberMute(final Sanction sanction) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(MUTE_DATA)) {
            preparedStatement.setLong(1, sanction.userId);
            preparedStatement.setString(2, sanction.userTag);
            preparedStatement.setString(3, sanction.userName);
            preparedStatement.setString(4, sanction.author);
            preparedStatement.setString(5, sanction.duration);
            preparedStatement.setString(6, sanction.reason);
            preparedStatement.setString(7, sanction.channelName);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logMemberStatus(final String query, final User member) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, member.getIdLong());
            preparedStatement.setString(2, member.getAsTag());
            preparedStatement.setString(3, member.getName());
            preparedStatement.setString(4, member.getEffectiveAvatarUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }

    public static void logUserBan(final Sanction sanction) {
        logSanction(BAN_DATA, sanction);
    }


    public static void logMemberWarn(final Sanction sanction) {
        logSanction(WARN_DATA, sanction);
    }

    public static void logException(final OccurredException occurredException) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(EXCEPTION_LOG)) {
            preparedStatement.setString(1, occurredException.occurredIn);
            preparedStatement.setString(2, occurredException.type);
            preparedStatement.setString(3, occurredException.details);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logVoiceChannelData(final String insertQuery, final VoiceChannel voiceChannel) {

        Connection connection = DatabaseConnector.openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, voiceChannel.memberId);
            preparedStatement.setString(2, voiceChannel.memberTag);
            preparedStatement.setString(3, voiceChannel.memberName);
            preparedStatement.setString(4, voiceChannel.name);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CommandHelper.class.getName()));
        }
    }

    public static void logInviteLinkTracking(final InviteTrackingData inviteTrackingData) {


        Connection connection = DatabaseConnector.openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(INVITE_TRACKING_LOG)) {
            preparedStatement.setString(1, inviteTrackingData.url);
            preparedStatement.setLong(2, inviteTrackingData.usedBy);
            preparedStatement.setLong(3, inviteTrackingData.invitedBy);
            preparedStatement.setInt(4, inviteTrackingData.uses);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CommandHelper.class.getName()));
        }
    }

    public static void logAdvertising(final long userId, final String userTag) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADVERTISING)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setBlob(2, Helper.changeCharacterEncoding(preparedStatement, userTag));
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            logException(OccurredException.getOccurredExceptionData(sqlException, CLASS_NAME));
        }
    }
}