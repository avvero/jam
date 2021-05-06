package pw.avvero.jiac;

import pw.avvero.jiac.schema.SchemaWriter;
import pw.avvero.jiac.schema.Issue;
import pw.avvero.jiac.terminal.ConsoleWriter;
import pw.avvero.jiac.terminal.Progress;

import java.util.concurrent.atomic.AtomicLong;

public class App {

    private static ConsoleWriter console = new ConsoleWriter();

    public static void main(String... args) throws Exception {
        console.newLine("App is started with args: " + String.join(", ", args));
        if (args.length == 0) {
            throw new IllegalArgumentException("Please specify feature file");
        }
        String host = args[0];
        String username = args[1];
        String password = args[2];
        String issueKey = args[3];
        console.newLine("Issue key is: " + issueKey);
        console.newLineBlueBold("------------------------------------------------------------------------------");
        Issue issue = new IssueApiDataProvider(host, username, password).getWithChildren(issueKey);
        console.newLineBlue(SchemaWriter.toString(issue));
        console.newLineBlueBold("------------------------------------------------------------------------------");
        final AtomicLong linePassed = new AtomicLong();
        final AtomicLong lastAffectionTimeNanos = new AtomicLong();
        final AtomicLong maxAffectionTimeNanos = new AtomicLong();
        new Progress(p -> {
            console.bottomLine(String.format(">Line passed: %s; flow impact milis: last %s , max %s;", linePassed.get(),
                    lastAffectionTimeNanos, maxAffectionTimeNanos));
        }, 100);
        console.endLine("End");
    }
}
