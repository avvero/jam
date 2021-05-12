package pw.avvero.jiac;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pw.avvero.jiac.core.Difference;
import pw.avvero.jiac.core.IssueDataProvider;
import pw.avvero.jiac.core.JiacService;
import pw.avvero.jiac.schema.Issue;
import pw.avvero.jiac.schema.SchemaWriter;
import pw.avvero.jiac.terminal.ConsoleWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

@Command
public class Commands implements Callable<Integer> {

    private static ConsoleWriter console = new ConsoleWriter();

    @Command(description = "Provide schema representation for the issue")
    public void schema(@Parameters(index = "0", description = "Key of the issue")
                               String key,
                       @Option(names = {"-c", "--configuration"},
                               description = "File with configuration",
                               defaultValue = "jiac.properties")
                               File file) throws IOException {
        JiacService service = getJiacService(file);
        Issue issue = service.getIssueWithChildren(key);
        console.newLineBlue(SchemaWriter.toString(issue));
    }

    @Command(description = "Checks schema representation with the issue on server")
    public void check(@Parameters(index = "0", description = "Key of the issue")
                              String key,
                      @Parameters(index = "1", description = "Target schema file")
                              String schemaFile,
                      @Option(names = {"-c", "--configuration"},
                              description = "File with configuration",
                              defaultValue = "jiac.properties")
                              File file) throws Exception {
        JiacService service = getJiacService(file);
        Issue from = service.getIssueWithChildren(key);
        Issue to = service.parseFromFile(schemaFile);
        List<Difference<?>> diff = service.diff(from, to);
        if (diff == null || diff.size() == 0) {
            console.newLineGreen("No difference");
        } else {
            console.newLineBlue("Differences: ");
            diff.forEach(d -> console.newLineBlue(" " + d));
        }
    }

    private JiacService getJiacService(File file) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(file));
        String host = prop.getProperty("host");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        IssueDataProvider dataProvider = new IssueApiDataProvider(host, username, password);
        return new JiacService(dataProvider);
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
