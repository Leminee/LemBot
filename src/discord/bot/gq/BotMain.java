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
    public static final String PREFIX = "?";
    public static final String TOKEN = "ODIwNDY4MDA5NzY0MjU3Nzky.YE1mYQ.XKdW2PNaC067MYtkMD3855JhfMM";

    public static void main(String[] args) {


        try {

            jda = JDABuilder.createDefault(TOKEN).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.GUILD_PRESENCES).build();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

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

    }
}
