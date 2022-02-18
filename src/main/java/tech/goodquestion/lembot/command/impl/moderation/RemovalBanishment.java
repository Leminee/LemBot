package tech.goodquestion.lembot.command.impl.moderation;

import tech.goodquestion.lembot.command.IBotCommand;

public abstract sealed class RemovalBanishment implements IBotCommand permits UnbanCommand,UnmuteCommand,UnwarnCommand{




}
