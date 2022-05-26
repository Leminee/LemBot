package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

import java.util.concurrent.TimeUnit;

public final class CodeBlockChecker extends ListenerAdapter {

    private static final String[] KEYWORDS = {
            "from",
            "break",
            "case",
            "switch",
            "int",
            "String",
            "class",
            "continue",
            "default",
            "do",
            "double",
            "else",
            "enum",
            "extends",
            "final",
            "finally",
            "float",
            "for",
            "goto",
            "if",
            "implements",
            "import",
            "instanceof",
            "interface",
            "long",
            "native",
            "new",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "strictfp",
            "super",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "transient",
            "try",
            "void",
            "volatile",
            "while",
            "true",
            "false",
            "null",
            "new",
            "import",
            "package",
    };

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        if (event.getMessage().getAttachments().size() != 0) return;

        final long channelId = event.getChannel().getIdLong();
        if (channelId == Config.getInstance().getChannelConfig().getGeneralChannel().getIdLong()) return;

        final String messageContent = event.getMessage().getContentRaw();
        final boolean isLongMessage = messageContent.length() >= 500;
        if (containsCode(messageContent) && isLongMessage && !containsCodeBlock(messageContent)) {

            final String content =
                            """
                            Deine Nachricht enthält möglicherweise Quellcode, aber keine Codeblöcke. Bitte füge zwecks besserer Lesbarkeit Codeblöcke hinzu!
                            Führe den folgenden Command aus, um angezeigt zu bekommen, wie das geht: `?hcb`
                            """;
            event.getMessage()
                    .reply(content)
                    .queue(m -> m.delete().queueAfter(5, TimeUnit.MINUTES));
        }

    }

    private boolean containsCode(final String messageContent) {

        final String[] words = messageContent.split(" ");

        for (final String keyword : KEYWORDS) {

            for (final String word : words) {

                if (word.equalsIgnoreCase(keyword)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean containsCodeBlock(final String messageContent) {

        return messageContent.startsWith("```") && messageContent.endsWith("```");
    }

}
