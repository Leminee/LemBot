package tech.goodquestion.lembot.command.impl.moderation;

import tech.goodquestion.lembot.command.IBotCommand;

public abstract sealed class SanctionRemoval implements IBotCommand permits UnbanCommand,UnmuteCommand,UnwarnCommand{

}
