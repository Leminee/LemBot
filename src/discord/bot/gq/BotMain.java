package discord.bot.gq;

import discord.bot.gq.db.BumpCounter;
import discord.bot.gq.db.StorageMemberJoin;
import discord.bot.gq.db.StorageMemberLeave;
import discord.bot.gq.db.UserMessageCounter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;
    public static String prefix = "?";
    public static final String token = "ODIwNDY4MDA5NzY0MjU3Nzky.YE1mYQ.BQBP6e26t0MI7MC4WERn0e4O_l";

    public static void main(String[] args) {

        try {

            jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.GUILD_PRESENCES).build();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.playing("Questions Game"));

        jda.addEventListener(new StorageMemberLeave());
        jda.addEventListener(new Clear());
        jda.addEventListener(new Answering());
        jda.addEventListener(new Reminder());
        jda.addEventListener(new MemberJoin());
        jda.addEventListener(new StorageMemberJoin());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());
        jda.addEventListener(new RoleSystem());

    }
}
