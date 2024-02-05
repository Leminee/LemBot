package tech.goodquestion.lembot.command.impl.moderation;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.UserSnowflake;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public final class UnmuteCommand implements IBotCommand, RemovalBanishment{


    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        if (args.length < 1) return;

        removeSanction(message, args);

        Member member = MuteCommand.getMemberFromCommandInput(message,args);

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setAuthor(member.getUser().getAsTag(), null,member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setDescription("**" + ":mute: Mute rückgängig gemacht" + "**");
        embedBuilder.addField("Member",member.getAsMention() , true);
        embedBuilder.addField("Rückgängig gemacht von",sender.getAsMention(), true);
        embedBuilder.setFooter(sender.getUser().getAsTag(),sender.getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        Helper.sendEmbed(embedBuilder,message,false);


        //TODO delete the mute from the database
    }

    @SuppressWarnings("null")
    @Override
    public void removeSanction(Message message, String[] args) {


        Member member = MuteCommand.getMemberFromCommandInput(message, args);


        Config.getInstance().getGuild().removeRoleFromMember(UserSnowflake.fromId(member.getIdLong()), Config.getInstance().getRoleConfig().getMuteRole()).queue();

        Connection connection = DatabaseConnector.openConnection();

        String userMuted = "SELECT activ FROM muted_user WHERE id_discord = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(userMuted)) {

            preparedStatement.setLong(1, member.getIdLong());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {


                String userUnmute = "UPDATE muted_user SET activ = ? WHERE id_discord = ? ORDER BY muted_at DESC LIMIT 1;";

                PreparedStatement preparedStatementOne = connection.prepareStatement(userUnmute);

                preparedStatementOne.setString(1, "0");
                preparedStatementOne.setLong(2, member.getIdLong());

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
        return "`unmute <member>`: Entfernt" + Config.getInstance().getRoleConfig().getMuteRole().getAsMention();
    }

    @Override
    public String getHelpList() {
        return "staff";
    }

}
