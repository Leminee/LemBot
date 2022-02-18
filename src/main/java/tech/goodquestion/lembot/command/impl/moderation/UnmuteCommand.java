package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UnmuteCommand extends RemovalBanishment implements IBotCommand {


    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        if (args.length < 1) {
            return;
        }

        User user;

        user = Helper.getUserFromCommandInput(message, args);


        Config.getInstance().getGuild().removeRoleFromMember(user.getIdLong(), Config.getInstance().getRoleConfig().getMuteRole()).queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setDescription("User " + user.getAsMention() + " wurde durch " + message.getAuthor().getAsMention() + " erfolgreich **" + " ungemutet." + "**");

        Helper.sendEmbed(embedBuilder, message, false);

        Connection connection = DatabaseConnector.openConnection();

        String userMuted = "SELECT activ FROM muted_user WHERE id_discord = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(userMuted)) {

            preparedStatement.setLong(1, user.getIdLong());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {


                String userUnmute = "UPDATE muted_user SET activ = ? WHERE id_discord = ? ORDER BY muted_at DESC LIMIT 1;";

                PreparedStatement preparedStatementOne = connection.prepareStatement(userUnmute);

                preparedStatementOne.setString(1, "0");
                preparedStatementOne.setLong(2, user.getIdLong());

                preparedStatementOne.executeUpdate();
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }

    }

    @Override
    public String getName() {
        return "unmute";
    }

    @Override
    public String getDescription() {
        return "`unmute <user> <reason>`: Entfernt" + Config.getInstance().getRoleConfig().getMuteRole().getAsMention();
    }


    @Override
    public String getHelpList() {
        return "staff";
    }
}
