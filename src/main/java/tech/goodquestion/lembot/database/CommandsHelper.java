package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.entities.Sanction;
import tech.goodquestion.lembot.lib.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CommandsHelper {



    private static final String INSERT_USER_STATUS = "INSERT INTO user_status (id_discord, user_tag, status) VALUES (?,?,?);";
    private static final String INSERT_MEMBER_AMOUNT = "INSERT INTO number_member (total_member) VALUES (?);";
    public static final String USER_LEAVE_LOG = "INSERT INTO user_leave (id_user_leave,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String USER_JOIN_LOG = "INSERT INTO user_join (id_user_join,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String USER_BAN_DATA = "INSERT INTO banned_user (id_banned_user,id_discord,user_tag, username, ban_author, ban_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)";
    public static final String USER_MUTE_DATA = "INSERT INTO muted_user (id_muted_user,id_discord,user_tag, username, mute_author, mute_reason, channel_name) " +
            "VALUES (NULL,?,?,?,?,?,?)";
    public static final String USER_WARN_DATA = "INSERT INTO warned_user (id_warned_user,id_discord,user_tag, username, warn_author, warn_reason, channel_name) " +
            "VALUES (NULL,?,?,?,?,?,?)";
    public static final String ACTIVE_MEMBER_LOG = "INSERT INTO active_user (active_member) VALUES (?);";

    public static final String ADJUSTING_NEW_USERNAME_IN_BUMPER = "UPDATE user_bump SET username = ? WHERE id_discord = ?;";
    public static final String ADJUSTING_NEW_USERNAME_IN_MESSAGE = "UPDATE user_message SET username = ? WHERE id_discord = ?;";
    public static final String USERNAME_UPDATED_LOG = "INSERT INTO updated_username (id_updated_username, id_discord, user_tag, old_username, new_username) VALUES (NULL,?,?,?,?);";
    public static final String REACTION_LOG = "INSERT INTO user_reaction (id_reaction, id_message, id_discord, reaction, count) VALUES (NULL,?,?,?,?);";

    private CommandsHelper(){

    }

    public static void logUserReactions (long messageId, long userId, String addedReaction, int count) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement statement = connection.prepareStatement(REACTION_LOG)) {
            statement.setLong(1, messageId);
            statement.setLong(2, userId);
            statement.setString(3, addedReaction);
            statement.setInt(4, count);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logMemberStatusChange(long userId, String userTag, OnlineStatus newStatus) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_USER_STATUS)) {
            statement.setLong(1, userId);
            statement.setBlob(2, Helper.changeCharacterEncoding(statement, userTag));
            statement.setString(3, String.valueOf(newStatus));
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logUpdatedUsername(long userId, String userTag, String oldUsername, String newUsername) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement statement = connection.prepareStatement(USERNAME_UPDATED_LOG)) {
            statement.setLong(1, userId);
            statement.setBlob(2, Helper.changeCharacterEncoding(statement, userTag));
            statement.setBlob(3, Helper.changeCharacterEncoding(statement, oldUsername));
            statement.setBlob(4, Helper.changeCharacterEncoding(statement,newUsername));
            statement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logActiveMemberCount(int memberCount) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ACTIVE_MEMBER_LOG)) {
            preparedStatement.setLong(1, memberCount);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logMemberAmount(Guild guild) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement statement = connection.prepareStatement(INSERT_MEMBER_AMOUNT)) {
            statement.setInt(1, guild.getMemberCount());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logUserLeave(User member) {
        QueryHelper.logMemberStatus(USER_LEAVE_LOG, member);
    }

    public static void logUserJoin(User member) {
        QueryHelper.logMemberStatus(USER_JOIN_LOG, member);
    }

    private static void logSanction(String query, Sanction sanction) {

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
        }
    }

    public static void logUserBan(Sanction sanction) {
        logSanction(USER_BAN_DATA, sanction);
    }

    public static void logUserMute(Sanction sanction) {
        logSanction(USER_MUTE_DATA, sanction);
    }

    public static void logUserWarn(Sanction sanction) {
        logSanction(USER_WARN_DATA, sanction);
    }
}
