package pw.avvero.jam;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pw.avvero.jam.core.Issue;
import pw.avvero.jam.core.IssueDataProvider;
import pw.avvero.jam.jira.HttpApiClient;
import pw.avvero.jam.jira.JiraApiDataProvider;
import pw.avvero.jam.schema.SchemaWriter;
import pw.avvero.jam.terminal.ConsoleWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;

@Command
public class Commands implements Callable<Integer> {

    private static ConsoleWriter console = new ConsoleWriter();

    @Command(description = "Provide schema representation for the issue")
    public void checkout(@Parameters(index = "0", description = "Key of the issue")
                                 String key,
                         @Option(names = {"-c", "--configuration"},
                                 description = "File with configuration",
                                 defaultValue = "jam.properties")
                                 File properties) throws IOException {
        JamService service = getJamService(properties);
        Issue issue = service.getIssueWithChildren(key);
        console.newLineBlue(SchemaWriter.toString(issue));
    }

    @Command(description = "Offer changes that schema represents to the issue")
    public void offer(@Parameters(index = "0", description = "Target schema file")
                              String schemaFile,
                      @Option(names = {"-c", "--configuration"},
                              description = "File with configuration",
                              defaultValue = "jam.properties")
                              File properties) throws Exception {
        JamService service = getJamService(properties);
        Issue to = service.parseFromFile(schemaFile);
        service.offer(to);
    }

    private JamService getJamService(File file) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(file));
        String host = prop.getProperty("host");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        IssueDataProvider dataProvider = new JiraApiDataProvider(new HttpApiClient(host, username, password));
        return new JamService(dataProvider);
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
