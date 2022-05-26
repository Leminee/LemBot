package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public final class CodeBlockChecker extends ListenerAdapter {

    private static final String [] KEYWORDS  = {
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


        final String messageContent = event.getMessage().getContentRaw();

        if (containsCode(messageContent) && messageContent.length() >= 150 && !containsCodeBlock(messageContent)) {

            event.getMessage()
                    .reply("""
                            Deine Nachricht enthält möglicherweise Quellcode, aber keine Codeblöcke. Bitte füge zwecks besserer Lesbarkeit Codeblöcke hinzu!
                            Führe den folgenden Command aus, um angezeigt zu bekommen, wie das geht: `?hcb`
                            """)
                    .queue( m -> m.delete().queueAfter(5, TimeUnit.MINUTES));
        }

    }


    private boolean containsCode(final String messageContent) {


        for (final String keyword : KEYWORDS) {

            if (messageContent.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsCodeBlock(final String messageContent) {

        return messageContent.startsWith("```") && messageContent.endsWith("```");
    }

}
