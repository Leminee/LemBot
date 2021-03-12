package discord.bot.gq;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;
    public static String prefix = "?";
    public static final String token = "ODE1ODk0ODA1ODk2ODg4MzYy.YDzDQA.qQ9MrVc1cQpef-nIx8rE99kuME0";

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
        jda.addEventListener(new Reply());
        jda.addEventListener(new Reminder());
        jda.addEventListener(new MemberJoin());
        jda.addEventListener(new StorageMemberJoin());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());

    }
}