package pw.avvero.jiac;

import java.util.concurrent.atomic.AtomicLong;

public class App {

    private static ConsoleWriter console = new ConsoleWriter();

    public static void main(String... args) throws Exception {
        console.newLine("App is started with args: " + String.join(", ", args));
        if (args.length == 0) {
            throw new IllegalArgumentException("Please specify feature file");
        }
        String featureFile = args[0];
        console.newLine("Feature file is: " + featureFile);
        console.newLineBlueBold("------------------------------------------------------------------------------");
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
