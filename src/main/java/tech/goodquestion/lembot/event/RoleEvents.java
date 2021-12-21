package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

import javax.annotation.Nonnull;
import java.util.Objects;

public class RoleEvents extends ListenerAdapter {

    @Override
    public void onRoleCreate(@Nonnull RoleCreateEvent event) {

       String newCreatedRole =  event.getRole().getAsMention();
       Objects.requireNonNull(event.getGuild()
               .getTextChannelById(Config.getInstance().getChannel().getLogChannel().getIdLong()))
               .sendMessage(String.format("Es wurde soeben folgende Rolle **erstellt** %s", newCreatedRole)).queue();
    }
    @Override
    public void onRoleDelete(@Nonnull RoleDeleteEvent event) {

        String deletedRole =  event.getRole().getName();
        Objects.requireNonNull(event.getGuild()
                .getTextChannelById(Config.getInstance().getChannel().getLogChannel().getIdLong()))
                .sendMessage(String.format("Es wurde soeben folgende Rolle **entfernt** `@%s`", deletedRole)).queue();
    }

    public void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {

        String oldName =  event.getOldName();
        String newName = event.getNewName();
        Objects.requireNonNull(event.getGuild()
                .getTextChannelById(Config.getInstance().getChannel().getLogChannel().getIdLong()))
                .sendMessage(String.format("Es wurde soeben folgende Rolle `@%s` in `@%s` umbenannt", oldName, newName)).queue();

    }

}
