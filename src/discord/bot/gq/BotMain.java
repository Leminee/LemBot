package discord.bot.gq;

import discord.bot.gq.db.*;
import discord.bot.gq.moderation.Delation;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;
    public static String prefix = "?";
    public static final String token = "ODIwNDY4MDA5NzY0MjU3Nzky.YE1mYQ.uTx2JA6tdaIUVzxtitYLcAvHgNs";

    public static void main(String[] args) {
        String channelId = "779107472622223400";
        String messageId = "821186181903024179";

        try {

            jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.GUILD_PRESENCES).build();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.playing("Questions Game"));

        jda.addEventListener(new StorageMemberLeave());
        jda.addEventListener(new Delation());
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

        ReactionManager manager = new ReactionManager(jda);

        manager.registerReaction(channelId, messageId, "821167071181275146", "784773593942327297").
                registerReaction(channelId, messageId, "821144439647895602", "811741950092116038").
                registerReaction(channelId, messageId, "821171703014621225", "784773877741387806");


                /*registerReaction(channelId, messageId, "821144409633849354", "808779281211719680").
                registerReaction(channelId, messageId, "821144363953553418", "809152859492974692").
                registerReaction(channelId, messageId, "821144354445328384", "815922232106156033").
                registerReaction(channelId, messageId, "821168328961163266", "808779520286654554").
                registerReaction(channelId, messageId, "821169196523192380", "808768626844893184").
                registerReaction(channelId, messageId, "821171704133845073", "808767910696189975");*/






    }
}
