package pw.avvero.jiac;

import pw.avvero.jiac.entity.Issue;
import pw.avvero.jiac.dsl.SchemaParser;
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
        String schemaFile = args[0];
        console.newLine("Schema file is: " + schemaFile);
        console.newLineBlueBold("------------------------------------------------------------------------------");
        Issue issue = new SchemaParser().parseFromFile(schemaFile);
        console.newLineBlue(issue.toString());
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
