package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.time.Instant;
import java.util.Objects;

public final class BanCommand extends UserBanishment {

    @SuppressWarnings("null")
    @Override
    public void banishUser(final User toBanish, final Sanction sanction, final Message originMessage) {

        final String performedSanction = SanctionType.BAN.getVerbalizedSanctionTyp();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));

        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setAuthor(toBanish.getAsTag(), null,toBanish.getEffectiveAvatarUrl());
        embedBuilder.setDescription("**" + ":no_entry: User gebannt" + "**");
        embedBuilder.addField("Gebannter User", toBanish.getAsMention(), true);
        embedBuilder.addField("Gebannt von", Objects.requireNonNull(originMessage.getMember()).getAsMention(), true);
        embedBuilder.addField("Grund",  "```" + sanction.reason + "```", false);
        embedBuilder.setFooter(originMessage.getMember().getUser().getAsTag(),originMessage.getMember().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        Helper.sendEmbed(embedBuilder,originMessage,false);

        notifyBannedUser(toBanish, performedSanction, sanction.reason, Objects.requireNonNull(originMessage.getMember()));

        Config.getInstance().getGuild().ban(toBanish,0,sanction.reason).queue();

        CommandHelper.logUserBan(sanction);
    }


      private void notifyBannedUser(final User sanctionedUser, final String performedSanction, final String reason, final Member sanctionAuthor) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        try {

            Helper.createEmbed(embedBuilder, ":no_entry: " + SanctionType.BAN, "Du wurdest auf **GoodQuestion** " + " **" + performedSanction + "**", EmbedColorHelper.BAN);
            embedBuilder.addField("Dauer", "permanent", true);
            embedBuilder.addField("Gebannt von", sanctionAuthor.getAsMention(), true);
            embedBuilder.addField("Grund", "```" + reason + "```", false);
            embedBuilder.setFooter(sanctionAuthor.getUser().getAsTag(), sanctionAuthor.getUser().getEffectiveAvatarUrl());
            embedBuilder.setTimestamp(Instant.now());

            sanctionedUser.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                    .complete();
        } catch (ErrorResponseException errorResponseException) {

            Objects.requireNonNull(Config.getInstance().getGuild()
                    .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                    .sendMessage(errorResponseException.getMessage() + " " +  sanctionedUser.getAsTag()).queue();

            System.out.println(errorResponseException.getMessage());
        }
    }
    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "`ban <user> <reason>`: Bannt den User";
    }

    @Override
    public boolean requiresAdmin() {
        return true;
    }
}
