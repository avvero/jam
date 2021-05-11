package pw.avvero.jiac;

import picocli.CommandLine;

public class App {

    public static void main(String... args) throws Exception {
        int exitCode = new CommandLine(new Commands()).execute(args);
        System.exit(exitCode);
    }
}
