package discord.bot.gq;

import discord.bot.gq.database.*;
import discord.bot.gq.moderation.InviteLinkDelation;
import discord.bot.gq.moderation.MessageDelation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;
    public static final String PREFIX = "?";
    public static final String TOKEN = "ODIwNDY4MDA5NzY0MjU3Nzky.YE1mYQ.R8aD7kMKl8_vamFEKtGFzHi9VJs";

    public static void main(String[] args) {

        String channelId = "779107472622223400";


        try {

            jda = JDABuilder.createDefault(TOKEN).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.GUILD_PRESENCES).build();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        jda.addEventListener(new StorageMemberLeave());
        jda.addEventListener(new MessageDelation());
        jda.addEventListener(new AutoAnswering());
        jda.addEventListener(new Reminder());
        jda.addEventListener(new MemberJoin());
        jda.addEventListener(new StorageMemberJoin());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());
        jda.addEventListener(new RoleSystem());
        jda.addEventListener(new TopBumper());
        jda.addEventListener(new TopFlooder());
        jda.addEventListener(new CommandList());
        jda.addEventListener(new DiscordListBumper());
        jda.addEventListener(new InviteLinkDelation());


        ReactionManager firstManager = new ReactionManager(jda);

        firstManager.registerReaction(channelId, "821186181903024179", "821167071181275146", "784773593942327297").
                registerReaction(channelId, "821186181903024179", "821144439647895602", "811741950092116038");


        ReactionManager secondManager = new ReactionManager(jda);
        String messageId = "832669953038614628";


        secondManager.registerReaction(channelId, messageId, "821144409633849354", "808779281211719680").
                registerReaction(channelId, messageId, "821144363953553418", "809152859492974692").
                registerReaction(channelId, messageId, "821169196523192380", "808768626844893184").
                registerReaction(channelId, messageId, "821171704133845073", "808767910696189975").
                registerReaction(channelId, messageId, "821168328961163266", "808779520286654554").
                registerReaction(channelId, messageId, "821144354445328384", "815922232106156033");

    }
}
