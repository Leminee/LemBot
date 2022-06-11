package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

import java.util.concurrent.TimeUnit;

public final class CodeBlockChecker extends ListenerAdapter {

    private static final String[] KEYWORDS = {
            "auto",
            "break",
            "case",
            "char",
            "const",
            "Integer",
            "continue",
            "default",
            "do",
            "double",
            "else",
            "enum",
            "extern",
            "float",
            "for",
            "goto",
            "if",
            "int",
            "long",
            "register",
            "return",
            "short",
            "signed",
            "sizeof",
            "static",
            "struct",
            "switch",
            "typedef",
            "union",
            "unsigned",
            "void",
            "volatile",
            "while",
            "_Packed",
            "abstract",
            "byte",
            "class",
            "extends",
            "import",
            "private",
            "throws",
            "assert",
            "final",
            "instanceof",
            "native",
            "protected",
            "synchronized",
            "transient",
            "while",
            "boolean",
            "and",
            "catch",
            "finally",
            "this",
            "try",
            "char",
            "implements",
            "interface",
            "package",
            "def",
            "elif",
            "except",
            "exec",
            "from",
            "global",
            "is",
            "in",
            "lambda",
            "not",
            "pass",
            "or",
            "print",
            "raise",
            "yield",
            "val",
            "var",
            "let",
            "typeof",
            "null",
            "function",
            "func",
            "foreach",
            "explicit",
            "sealed",
            "ulong",
            "unchecked",
            "base",
            "checked",
            "unsafe",
            "readonly",
            "operator",
            "fixed",
            "out",
            "ref",
            "decimal",
            "uint",
            "<head>",
            "<title>",
            "<body>",
            "<html>",
            "<meta>",
            "<link>",
            "<style>",
            "<script>",
            "<div>",
            "<span>",
            "<br>",
            "<hr>",
            "<h1>",
            "<h2>",
            "<h3>",
            "<h4>",
            "<h5>",
            "<h6>",
            "<p>",
            "<a>",
            "<i>",
            "<u>",
            "<strong>",
            "<em>",
            "<ol>",
            "<ul>",
            "<li>",
            "<dl>",
            "<dt>",
            "<dd>",
            "<table>",
            "<tr>",
            "<th>",
            "<td>",
            "<tbody>",
            "<thead>",
            "<tfoot>",
            "<col>",
            "<colgroup>",
            "<caption>",
            "<form>",
            "<input>",
            "<button>",
            "<select>",
            "<option>",
            "<textarea>",
            "<label>",
            "<fieldset>",
            "<legend>",
    };

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        if (event.getMessage().getAttachments().size() != 0) return;

        final long channelId = event.getChannel().getIdLong();
        if (channelId == Config.getInstance().getChannelConfig().getGeneralChannel().getIdLong()) return;

        final String messageContent = event.getMessage().getContentRaw();
        if (containsCode(messageContent) && isLongMessage(messageContent) && !containsCodeBlock(messageContent)) {

            final String content =
                    """
                            Deine Nachricht enthält möglicherweise Quellcode, aber keine Codeblöcke. Bitte füge zwecks besserer Lesbarkeit Codeblöcke hinzu!
                            Führe den folgenden Command aus, um angezeigt zu bekommen, wie das geht: `?hcb`
                            """;
            event.getMessage().reply(content).queue(m -> m.delete().queueAfter(5, TimeUnit.MINUTES));
        }

    }

    private boolean containsCode(final String messageContent) {

        int probability = 0;

        final String[] words = messageContent.split(" ");
        final String[] lines = messageContent.split("\n");

        for (final String word : words) {

            for (final String keyword : KEYWORDS) {

                if (word.equalsIgnoreCase(keyword)) probability += 5;
            }
        }

        for (final String line : lines) {

            if (line.startsWith(" ")) probability += 5;

            if (line.contains("{") || line.contains("}")) probability += 5;

            if (line.contains("=") || line.contains("()")) probability += 5;
        }


        return probability >= 95;
    }

    private boolean containsCodeBlock(final String messageContent) {

        final int firstOccurrence = messageContent.indexOf("```");
        final int lastOccurrence = messageContent.lastIndexOf("```");

        return (firstOccurrence != -1) && (lastOccurrence != firstOccurrence);
    }

    private boolean isLongMessage(final String messageContent) {

        final String[] lines = messageContent.split("\n");

    	return lines.length >= 10;
    }

}
