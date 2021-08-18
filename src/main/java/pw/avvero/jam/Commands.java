package pw.avvero.jam;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import pw.avvero.jam.core.Issue;
import pw.avvero.jam.core.IssueDataProvider;
import pw.avvero.jam.core.IssueLink;
import pw.avvero.jam.graphviz.GraphvizWriter;
import pw.avvero.jam.jira.HttpApiClient;
import pw.avvero.jam.jira.JiraApiDataProvider;
import pw.avvero.jam.schema.SchemaWriter;
import pw.avvero.jam.terminal.ConsoleWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;

@Slf4j
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

    @Command(description = "Provide graphviz representation for issue's dependencies")
    public void dependencies(@Parameters(index = "0", description = "Key of the issue")
                                     String key,
                             @Option(names = {"-c", "--configuration"},
                                     description = "File with configuration",
                                     defaultValue = "jam.properties")
                                     File properties) throws IOException {
        JamService service = getJamService(properties);
        Issue issue = service.getIssueWithChildren(key);
        // TODO custom jira structure specifique
        if ("Initiative".equalsIgnoreCase(issue.getType()) && issue.getLinks() != null) {
            for (IssueLink link : issue.getLinks()) {
                if ("is parent task of".equals(link.getType())) {
                    log.debug("Take details from linked issue: {} {}", link.getType(), link.getIssue().getKey());
                    Issue enriched = service.getIssueWithChildren(link.getIssue().getKey());
                    issue.getChildren().add(enriched);
                }
            }
        }
        console.newLineBlue(GraphvizWriter.toString(issue));
    }

    private JamService getJamService(File file) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(file));
        String host = prop.getProperty("host");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        int connectTimeout = Integer.parseInt(prop.getProperty("connect-timeout", "30000"));
        IssueDataProvider dataProvider = new JiraApiDataProvider(new HttpApiClient(host, username, password,
                connectTimeout));
        return new JamService(dataProvider);
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
