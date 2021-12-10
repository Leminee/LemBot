package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import tech.goodquestion.lembot.entities.Sanction;
import tech.goodquestion.lembot.lib.Helper;

import java.sql.*;
import java.util.Objects;

public final class QueryHelper {

    private static final String INSERT_USER_STATUS = "INSERT INTO user_status (id_discord, user_tag, status) VALUES (?,?,?);";
    private static final String INSERT_MEMBER_AMOUNT = "INSERT INTO number_member (total_member) VALUES (?);";
    public static final String TOP_EMOJIS = """
            SELECT '\uD83D\uDC4D', SUM(id_discord) AS c  FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDC4D%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T
            UNION ALL\s
            SELECT '\uD83D\uDE05', SUM(id_discord) AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE05%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            UNION ALL  \s
            SELECT '\uD83D\uDE0A', SUM(id_discord )AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE0A%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            UNION ALL
            SELECT '\uD83D\uDC40', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDC40%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            UNION ALL
            SELECT '\uD83D\uDE02', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE02%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            UNION ALL
            SELECT '\uD83D\uDE09', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE09%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            UNION ALL
            SELECT '\uD83D\uDE1B', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE1B%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T \s
            UNION ALL
            SELECT content, SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE42%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            UNION ALL
            SELECT '\uD83D\uDE42', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE43%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            UNION ALL
            SELECT '🔴' as emoji, SUM(id_discord) AS c FROM (SELECT content, Count(id_discord) AS id_discord FROM `user_message_content` WHERE  content LIKE '%🔴%') AS T
            UNION ALL
            SELECT '🟢' as emoji, SUM(id_discord) AS c FROM (SELECT content, Count(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%🟢%') AS T
            UNION ALL
            SELECT '\uD83E\uDD13', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83E\uDD13%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T \s
            UNION ALL
            SELECT '\uD83D\uDE04', SUM(id_discord)AS c FROM (SELECT content, COUNT(id_discord) AS id_discord FROM `user_message_content` WHERE content LIKE '%\uD83D\uDE04%' GROUP BY content ORDER BY COUNT(id_discord) DESC) AS T\s
            ORDER BY c DESC LIMIT 3;""";
    public static final String TOP_CHANNELS = "SELECT channel_name, COUNT(id_channel) FROM `channel` GROUP BY id_channel ORDER BY COUNT(id_channel) DESC LIMIT 5;";
    public static final String USER_LEAVE_LOG = "INSERT INTO user_leave (id_user_leave,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String USER_JOIN_LOG = "INSERT INTO user_join (id_user_join,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";
    public static final String USER_BAN_DATA = "INSERT INTO banned_user (id_banned_user,id_discord,user_tag, username, ban_author, ban_reason, channel_name) VALUES (NULL,?,?,?,?,?,?)";
    public static final String USER_MUTE_DATA = "INSERT INTO muted_user (id_muted_user,id_discord,user_tag, username, mute_author, mute_reason, channel_name) " +
            "VALUES (NULL,?,?,?,?,?,?)";
    public static final String USER_WARN_DATA = "INSERT INTO warned_user (id_warned_user,id_discord,user_tag, username, warn_author, warn_reason, channel_name) " +
            "VALUES (NULL,?,?,?,?,?,?)";
    public static final String NEXT_BUMP_TIME = "SELECT TIME(TIMESTAMPADD(HOUR,2, bumped_on)) FROM user_bump_time ORDER BY bumped_on DESC LIMIT 1";
    public static final String NEXT_BUMP = "SELECT TIMESTAMPDIFF(MINUTE,CURRENT_TIMESTAMP, TIMESTAMPADD(HOUR,2, bumped_on)) FROM user_bump_time ORDER BY bumped_on DESC LIMIT 1";
    public static final String ACTIVE_MEMBER_LOG = "INSERT INTO active_user (active_member) VALUES (?);";
    public static final String ACTIVE_USER_RECORD = "SELECT MAX(active_member) FROM active_user;";
    public static final String USERNAME_UPDATED_LOG = "INSERT INTO updated_username (id_updated_username, id_discord, user_tag, old_username, new_username) VALUES (NULL,?,?,?,?);";
    public static final String ADJUSTING_NEW_USERNAME_IN_BUMPER = "UPDATE user_bump SET username = ? WHERE id_discord = ?;";
    public static final String ADJUSTING_NEW_USERNAME_IN_MESSAGE = "UPDATE user_message SET username = ? WHERE id_discord = ?;";
    public static final String TOP_MONTHLY_BUMPER = "SELECT username, COUNT(user_bump_time.id_discord) FROM `user_bump_time` INNER JOIN user_bump ON user_bump_time.id_discord = user_bump.id_discord  WHERE bumped_on > (SELECT DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH)) GROUP BY user_bump_time.id_discord ORDER BY COUNT(user_bump_time.id_discord) DESC LIMIT 3";
    public static final String TOP_MONTHLY_FLOODER = "SELECT username, COUNT(user_message_content.id_discord) FROM `user_message_content` INNER JOIN user_message ON user_message_content.id_discord = user_message.id_discord  WHERE posted_on > (SELECT DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH)) GROUP BY user_message.id_discord ORDER BY COUNT(user_message_content.id_discord) DESC LIMIT 3;";
    public static final String TOP_BUMPER = "SELECT username FROM user_bump ORDER BY number_bumps DESC, username LIMIT 3;";
    public static final String TOP_FLOODER = "SELECT username FROM user_message ORDER BY number_message DESC LIMIT 3;";
    public static final String AMOUNT_MESSAGES = "SELECT number_message FROM user_message WHERE id_discord = ?";
    public static final String NEXT_HIGHER_USER_AMOUNT_MESSAGES = "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1";
    public static final String SPAM_VERIFICATION = "SELECT COUNT(DISTINCT id_channel) FROM `channel` INNER JOIN user_message_content ON channel.id_message = user_message_content.id_message WHERE user_message_content.id_discord = ? AND content = ? AND posted_on >= NOW() - INTERVAL 1 MINUTE";
    public static final String SPAM_DATA = "SELECT id_channel, channel.id_message FROM `channel` INNER JOIN user_message_content ON channel.id_message = user_message_content.id_message WHERE user_message_content.id_discord = ? AND content = ? AND posted_on >= NOW() - INTERVAL 1 MINUTE";
    public static final String AMOUNT_SPAM_MESSAGES = "SELECT COUNT(id_discord) FROM user_message_content WHERE id_discord = ? AND content = ? AND posted_on >= NOW() - INTERVAL 30 SECOND";

    private QueryHelper() {

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

    private static EmbedBuilder getTop(String query, String[] fieldNames, String[] fieldIcons) throws SQLException {

        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription("");

            int top = 1;

            while (resultSet.next()) {
                if (top == 1) {
                    embed.addField(fieldNames[0], fieldIcons[0] + resultSet.getString(1), false);
                }
                if (top == 2) {
                    embed.addField(fieldNames[1], fieldIcons[1] + resultSet.getString(1), false);
                }
                if (top == 3) {
                    embed.addField(fieldNames[2], fieldIcons[2] + resultSet.getString(1), false);
                }
                top++;
            }
            return embed;
        }
    }

    public static EmbedBuilder getTopEmojis() throws SQLException {
        return getTop(TOP_EMOJIS, new String[]{"**TOP 1**", "**TOP 2**", "**TOP 3**"}, new String[]{"", "", ""});
    }

    public static EmbedBuilder getTopActiveChannels() throws SQLException {
        return getTop(TOP_CHANNELS, new String[]{"**TOP 1**", "**TOP 2**", "**TOP 3**"}, new String[]{"", "", ""});
    }


    private static void logMemberStatus(String query, User member) {
        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, member.getIdLong());
            preparedStatement.setString(2, member.getAsTag());
            preparedStatement.setString(3, member.getName());
            preparedStatement.setString(4, member.getEffectiveAvatarUrl());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void logUserLeave(User member) {
        logMemberStatus(USER_LEAVE_LOG, member);
    }

    public static void logUserJoin(User member) {
        logMemberStatus(USER_JOIN_LOG, member);
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

    public static Time getNextBumpTime() {

        Time nextBumpTime = null;

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(NEXT_BUMP_TIME)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nextBumpTime = resultSet.getTime(1);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return nextBumpTime;
    }

    public static int getMinutesToNextBump() {

        int minutesBeforeBump = 0;

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(NEXT_BUMP)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                minutesBeforeBump = resultSet.getInt(1);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return minutesBeforeBump;
    }

    public static int getActiveUserRecord() {

        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(ACTIVE_USER_RECORD)) {


            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return 0;
    }


    public static void adjustUsername(String adjustingDateQuery, String newUsername, long userId) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(adjustingDateQuery)) {

            preparedStatement.setString(1, newUsername);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static boolean isActiveUserRecord(int approximatePresentMember) {


        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(ACTIVE_USER_RECORD)) {


            if (resultSet.next()) {

                int currentActiveUseRecord = resultSet.getInt(1);

                if (approximatePresentMember > currentActiveUseRecord) {
                    return true;
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }

    public static boolean isSpammer(long userId, String messageContent) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SPAM_VERIFICATION)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, messageContent);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                int amountResult = resultSet.getInt(1);

                if (amountResult >= 3) {
                    return true;
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }


    public static void deleteSpammerMessages(GuildMessageReceivedEvent event, long userId, String messageContent) {


        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SPAM_DATA)) {


            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, messageContent);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Objects.requireNonNull(event.getGuild()
                        .getTextChannelById(resultSet.getLong("id_channel")))
                        .deleteMessageById(resultSet.getLong("id_message"))
                        .queue();

            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        event.getMessage().delete().queue();
    }

    public static boolean areToManyMessages(long userId, String messageContent) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(AMOUNT_SPAM_MESSAGES)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, messageContent);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                int amountResult = resultSet.getInt(1);

                if (amountResult >= 10) {
                    return true;
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }
}
