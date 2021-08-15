package discord.bot.gq;

import discord.bot.gq.command.*;
import discord.bot.gq.command.db.*;
import discord.bot.gq.command.moderation.MessageDelation;
import discord.bot.gq.command.moderation.UserBanishment;
import discord.bot.gq.config.command.UpdatingChannel;
import discord.bot.gq.config.command.UpdatingRole;
import discord.bot.gq.database.*;
import discord.bot.gq.event.DiscordListBumper;
import discord.bot.gq.event.InviteLinkDelation;
import discord.bot.gq.event.Reminder;
import discord.bot.gq.event.WelcomingMemberJoin;
import discord.bot.gq.lib.Helper;
import discord.bot.gq.lib.ReactionManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;

    public static void main(String[] args) {

        String channelId = "779107472622223400";
        String messageId = "832669953038614628";

        try {

            jda = JDABuilder.createDefault(Helper.TOKEN).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.GUILD_PRESENCES).build();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        jda.addEventListener(new MemberLeaveStorage());
        jda.addEventListener(new MessageDelation());
        jda.addEventListener(new AutoAnswering());
        jda.addEventListener(new Reminder());
        jda.addEventListener(new WelcomingMemberJoin());
        jda.addEventListener(new MemberJoinStorage());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());
        jda.addEventListener(new RoleSystem());
        jda.addEventListener(new TopBumperSelection());
        jda.addEventListener(new TopFlooderSelection());
        jda.addEventListener(new HelpList());
        jda.addEventListener(new DiscordListBumper());
        jda.addEventListener(new InviteLinkDelation());
        jda.addEventListener(new PasswordCheck());
        jda.addEventListener(new UpdatingChannel());
        jda.addEventListener(new UpdatedMessageStorage());
        jda.addEventListener(new DeletedMessageStorage());
        jda.addEventListener(new UpdatingRole());
        jda.addEventListener(new NumberMessagesCheck());
        jda.addEventListener(new NumberBumpsCheck());
        jda.addEventListener(new TopListReminder());
        jda.addEventListener(new ActiveUsers());
        jda.addEventListener(new ActiveUserRecord());
        jda.addEventListener(new TopPingedUser());
        jda.addEventListener(new TopUsedEmojis());
        jda.addEventListener(new ChannelMessageCounter());
        jda.addEventListener(new TopActiveChannel());
        jda.addEventListener(new UserBanishment());
        jda.addEventListener(new VoiceJoinedStorage());
        jda.addEventListener(new VoiceLeavedStorage());


      ReactionManager firstManager = new ReactionManager(jda);


        firstManager.registerReaction(channelId, "821186181903024179", "821167071181275146", "784773593942327297").
                registerReaction(channelId, "821186181903024179", "821144439647895602", "811741950092116038");


        ReactionManager secondManager = new ReactionManager(jda);


        secondManager.registerReaction(channelId, messageId, "821144409633849354", "808779281211719680").
                registerReaction(channelId, messageId, "821144363953553418", "809152859492974692").
                registerReaction(channelId, messageId, "821169196523192380", "808768626844893184").
                registerReaction(channelId, messageId, "821171704133845073", "808767910696189975").
                registerReaction(channelId, messageId, "821168328961163266", "808779520286654554").
                registerReaction(channelId, messageId, "846816752867868742", "846812921375359027").
                registerReaction(channelId, messageId, "846854659104833566", "846856698979418152");
    }
}
