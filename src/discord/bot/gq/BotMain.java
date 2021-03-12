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
    public static final String token = "ODE1ODk0ODA1ODk2ODg4MzYy.YDzDQA.UlqIE-Hcc34gtXIBo1H-AFI1a5U";

    public static void main(String[] args) {

        try {

            jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.GUILD_PRESENCES).build();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.playing("Questions Game"));

        jda.addEventListener(new StoreMemberLeave());
        jda.addEventListener(new Clear());
        jda.addEventListener(new Reply());
        jda.addEventListener(new RemindMe());
        jda.addEventListener(new MemberJoin());
        jda.addEventListener(new StoreMemberJoin());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());

    }
}