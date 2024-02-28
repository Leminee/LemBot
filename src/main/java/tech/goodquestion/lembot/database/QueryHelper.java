package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


public final class QueryHelper {

    public static final String TOP_CHANNELS = "SELECT id_channel, COUNT(id_channel) FROM `channel` GROUP BY id_channel ORDER BY COUNT(id_channel) DESC LIMIT 5;";
    public static final String NEXT_BUMP_TIME = "SELECT TIME(TIMESTAMPADD(HOUR,2, bumped_at)) FROM user_bump_time ORDER BY bumped_at DESC LIMIT 1";
    public static final String NEXT_BUMP = "SELECT TIMESTAMPDIFF(MINUTE,CURRENT_TIMESTAMP, TIMESTAMPADD(HOUR,2, bumped_at)) FROM user_bump_time ORDER BY bumped_at DESC LIMIT 1";
    public static final String ACTIVE_USER_RECORD = "SELECT MAX(user_online.amount) FROM user_online;";
    public static final String TOP_MONTHLY_BUMPER = "SELECT user_bump_time.id_discord, COUNT(user_bump_time.id_discord) FROM `user_bump_time` INNER JOIN user_bump ON user_bump_time.id_discord = user_bump.id_discord  WHERE bumped_at > (SELECT DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH)) GROUP BY user_bump_time.id_discord ORDER BY COUNT(user_bump_time.id_discord) DESC LIMIT 3";
    public static final String TOP_MONTHLY_FLOODER = "SELECT user_message.id_discord, COUNT(user_message_content.id_discord) FROM `user_message_content` INNER JOIN user_message ON user_message_content.id_discord = user_message.id_discord  WHERE posted_at > (SELECT DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 MONTH)) GROUP BY user_message.id_discord ORDER BY COUNT(user_message_content.id_discord) DESC LIMIT 3;";
    public static final String TOP_BUMPER = "SELECT id_discord, number_bumps FROM user_bump ORDER BY number_bumps DESC, username LIMIT 3;";
    public static final String TOP_FLOODER = "SELECT id_discord, number_message FROM user_message ORDER BY number_message DESC LIMIT 3;";
    public static final String AMOUNT_MESSAGES = "SELECT number_message FROM user_message WHERE id_discord = ?";
    public static final String NEXT_HIGHER_USER_AMOUNT_MESSAGES = "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1";
    public static final String SPAM_VERIFICATION = "SELECT COUNT(DISTINCT id_channel) FROM `channel` INNER JOIN user_message_content ON channel.id_message = user_message_content.id_message WHERE user_message_content.id_discord = ? AND content = ? AND posted_at >= NOW() - INTERVAL 30 SECOND";
    public static final String SPAM_DATA = "SELECT id_channel, channel.id_message FROM `channel` INNER JOIN user_message_content ON channel.id_message = user_message_content.id_message WHERE user_message_content.id_discord = ? AND content = ? AND posted_at >= NOW() - INTERVAL 30 SECOND";
    public static final String HOPPING_CHECK = "SELECT COUNT(id_discord) FROM voice_join WHERE id_discord = ? AND joined_at >= NOW() - INTERVAL 60 SECOND UNION ALL SELECT COUNT(id_discord) FROM voice_move WHERE id_discord = ? AND moved_in_at >= NOW() - INTERVAL 60 SECOND";
    private static final String ADVERTISING_CHECK = "SELECT * FROM advertising WHERE id_discord = ?" ;
    private static final String LAST_ACTIVITY = "SELECT * FROM((SELECT changed_at FROM `user_status` WHERE id_discord = ? AND status = 'OFFLINE' ORDER BY changed_at DESC LIMIT 1) UNION ALL (SELECT posted_at FROM `user_message_content` WHERE id_discord = ? ORDER BY `posted_at` DESC LIMIT 1)) t ORDER BY t.changed_at DESC";
    private static final String AMOUNT_WARNS = "SELECT COUNT(id_discord) FROM warned_user WHERE id_discord = ?";
    private static final String AMOUNT_MUTES = "SELECT COUNT(id_discord) FROM muted_user WHERE id_discord = ?";
    private static final String AMOUNT_BANS= "SELECT COUNT(id_discord) FROM banned_user WHERE id_discord = ?" ;
    private static final String INVITER = "SELECT invited_by FROM invite_tracking WHERE used_by = ?";
    private static final String ACTIVE_SANCTION = "SELECT activ FROM `muted_user` WHERE id_discord = ? ORDER BY activ DESC LIMIT 1";
    private static final String CONTRIBUTORS ="SELECT mention FROM contributor ORDER BY contributor_since" ;
    private static final String SANCTION_HISTORY = "SELECT * FROM ((SELECT ':warning: VERWARNT am ' , warned_at, ' durch ', warn_author, ' mit folgender Begründung:', CONCAT('```',warned_user.reason, '```') FROM warned_user WHERE warned_user.id_discord = ?) UNION ALL (SELECT CONCAT(':mute: GEMUTET ', '**', duration, '**', ' am ') , muted_at, ' durch ' , mute_author, ' mit folgender Begründung:' , CONCAT('```',muted_user.reason,'```') FROM muted_user WHERE muted_user.id_discord = ?) UNION ALL (SELECT ':no_entry: GEBANNT am ' , banned_at, ' durch ' , ban_author, ' mit folgender Begründung:' , CONCAT('```',banned_user.reason,'```') FROM banned_user WHERE banned_user.id_discord = ?)) AS t ORDER BY t.warned_at";
    public static String MESSAGE_COUNT = "SELECT COUNT(user_message_content.id_discord) + 40000 FROM user_message_content";
    public static String ADMINS_MENTIONED = "SELECT mention FROM staff WHERE role_name = 'Administrator' ORDER BY staff_since;";
    public static String MODERATORS_MENTIONED = "SELECT mention FROM staff WHERE role_name = 'Moderator' ORDER BY staff_since;";
    public static final String AMOUNT_BUMPS = "SELECT number_bumps FROM user_bump WHERE id_discord = ?";
    //SELECT number_bumps AS b, (SELECT COUNT(id_discord) FROM user_bump WHERE number_bumps > (b)) +1 AS 'TOP' FROM `user_bump` WHERE id_discord = ?
    public static final String NEXT_HIGHER_USER_AMOUNT_BUMPS = "SELECT id_discord, number_bumps FROM user_bump WHERE number_bumps > ? ORDER BY number_bumps, username LIMIT 1";
    public static final List<String> adminsAsMention = new ArrayList<>();
    public static final List<String> moderatorsAsMention = new ArrayList<>();
    public static final List<String> contributorsAsMention = new ArrayList<>();
    public static String FIRST_CONTENT_AFTER_UPDATING_MESSAGE = "SELECT content FROM user_message_content WHERE id_message = ? ";
    public static String UPDATED_MESSAGE_LAST_CONTENT = "SELECT content FROM updated_message WHERE id_message = ? ORDER BY updated_at DESC LIMIT 1 ";
    public static String RAID_DETECTION = "SELECT COUNT(DISTINCT id_discord) FROM `user_join` WHERE joined_at >= NOW() - INTERVAL 30 SECOND";


    private QueryHelper() {

    }


    public static EmbedBuilder getTopChannels() throws SQLException {

        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(QueryHelper.TOP_CHANNELS)) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription("");

            int top = 1;

            while (resultSet.next()) {


                if (top == 1) {
                    embed.addField("**" + "TOP " + top + "**", "<#" + resultSet.getLong(1) + ">", false);
                }
                if (top == 2) {
                    embed.addField("**" + "TOP " + top + "**", "<#" + resultSet.getLong(1) + ">", false);
                }
                if (top == 3) {
                    embed.addField("**" + "TOP " + top + "**", "<#" + resultSet.getLong(1) + ">", false);
                }

                if (top == 4) {
                    embed.addField("**" + "TOP " + top + "**", "<#" + resultSet.getLong(1) + ">", false);
                }
                if (top == 5) {
                    embed.addField("**" + "TOP " + top + "**", "<#" + resultSet.getLong(1) + ">", false);
                }


                top++;
            }
            return embed;
        }
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
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
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

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return minutesBeforeBump;
    }

    public static int getActiveUserRecord() {

        return getServerData(ACTIVE_USER_RECORD);
    }


    public static boolean isActiveUserRecord(final int approximatePresentMember) {


        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(ACTIVE_USER_RECORD)) {


            if (resultSet.next()) {

                final int currentActiveUseRecord = resultSet.getInt(1);

                if (approximatePresentMember > currentActiveUseRecord) {
                    return true;
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }
        return false;
    }

    public static boolean isSpammer(final long userId, final String messageContent) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SPAM_VERIFICATION)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, messageContent);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                final int amountResult = resultSet.getInt(1);

                if (amountResult >= 3) {
                    return true;
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }


    public static void deleteScammerMessages(final MessageReceivedEvent event, final long userId, final String messageContent) {


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

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

    }


    public static boolean isHopper(final long userId) {


        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(HOPPING_CHECK)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            int amountHops = 0;

            while (resultSet.next()) {

                amountHops += resultSet.getInt(1);
            }

            if (amountHops >= 10) {
                return true;
            }


        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }
        return false;

    }

    public static boolean isServerUnderAttack() {


        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(RAID_DETECTION)) {


            if (resultSet.next()) {

                final int amountNewMember = resultSet.getInt(1);

                if (amountNewMember >= 10) {
                    return true;
                }
            }

        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }
        return false;

    }

    public static int getMessagesCount() {

        return getServerData(MESSAGE_COUNT);
    }

    public static int getServerData(String serverData) {

        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(serverData)) {

            if (resultSet.next()) {

                return resultSet.getInt(1);

            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }
        return 0;
    }

    public static List<String> getAdminsAsMention() {

        if (adminsAsMention.size() > 0) return adminsAsMention;

        return getStaffAsMention(ADMINS_MENTIONED, adminsAsMention);

    }

    public static List<String> getStaffAsMention(final String adminIds, final List<String> adminsAsMention) {
        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(adminIds)) {


            while (resultSet.next()) {

                adminsAsMention.add(resultSet.getString(1));

            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return adminsAsMention;
    }

    public static List<String> getModeratorsAsMention() {

        if (moderatorsAsMention.size() > 0) return moderatorsAsMention;

        return getStaffAsMention(MODERATORS_MENTIONED, moderatorsAsMention);

    }

    public static String getUpdatedMessageOldContent(final long updatedMessageId, final String query) {

        String oldContent = "";

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, updatedMessageId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                oldContent = resultSet.getString(1);

            } else {

                return getUpdatedMessageOldContent(updatedMessageId, FIRST_CONTENT_AFTER_UPDATING_MESSAGE);
            }


        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return oldContent;
    }

    public static boolean hasAlreadyReceivedAdvertising(final long userid) {


       try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADVERTISING_CHECK)) {

           preparedStatement.setLong(1, userid);

           ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return true;

            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }
        return false;

    }

    public static LocalDateTime getLastActivityDateTimeBy(long userId) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(LAST_ACTIVITY)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();


            LocalDateTime lastActivityDateTime;

            if (resultSet.next()) {


                lastActivityDateTime = resultSet
                        .getTimestamp(1, Calendar.getInstance(TimeZone.getDefault()))
                        .toLocalDateTime();

               return lastActivityDateTime;
            }


        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return null;
    }

    public static long getAmountMessagesBy(long userId) {

        return getAmountOfBy(userId, AMOUNT_MESSAGES);
    }

    public static long getAmountOfBy(long userId, String amountOf) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(amountOf)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            int amountMessages;

            if (resultSet.next()) {

                amountMessages = resultSet.getInt(1);

                return amountMessages;
            }

        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return 0;
    }

    public static long getAmountBumpsBy(long userId) {

        return getAmountOfBy(userId, AMOUNT_BUMPS);
    }


    public static long getAmountWarns(long userId){
        return getAmountOfBy(userId,AMOUNT_WARNS);
    }

    public static long getAmountMutes(long userId){
        return getAmountOfBy(userId,AMOUNT_MUTES);
    }

    public static long getAmountBans(long userId){
        return getAmountOfBy(userId,AMOUNT_BANS);
    }

    public static String getInviter(long userId) {

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INVITER)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return "<@" + resultSet.getString(1) + ">";
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return "N/A";
    }

    public static boolean hasActiveSanction(long userId){

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ACTIVE_SANCTION)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {

                return resultSet.getInt(1) == 1;
            }

        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return false;
    }

    public static List<String> getContributorsAsMention() {

        if (contributorsAsMention.size() > 0) return contributorsAsMention;

        try (Connection connection = DatabaseConnector.openConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(CONTRIBUTORS)) {

            while (resultSet.next()) {

                contributorsAsMention.add(resultSet.getString(1));
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return contributorsAsMention;
    }

    public static String getSanctionHistoryBy(final long userId){


        final StringBuilder sanctionHistory = new StringBuilder();

        try (Connection connection = DatabaseConnector.openConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SANCTION_HISTORY)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                sanctionHistory
                        .append(resultSet.getString(1))
                        .append("**")
                        .append(LocalDateTimeFormatter.toGermanFormat(resultSet.getTimestamp(2, Calendar.getInstance(
                                TimeZone.getDefault()
                        )).toLocalDateTime()))
                        .append("**")
                        .append(resultSet.getString(3))
                        .append("**")
                        .append(resultSet.getString(4))
                        .append("**")
                        .append(resultSet.getString(5))
                        .append(resultSet.getString(6))
                        .append("\n");
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, QueryHelper.class.getName()));
        }

        return sanctionHistory.length() > 0 ? sanctionHistory.toString() : "```Keine Sanktionen```";
    }

}
