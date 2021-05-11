package pw.avvero.jiac.core;

import lombok.RequiredArgsConstructor;
import pw.avvero.jiac.schema.Issue;
import pw.avvero.jiac.schema.SchemaParser;
import pw.avvero.jiac.schema.SchemaParsingError;
import pw.avvero.jiac.schema.SchemaWriter;

import java.util.List;

@RequiredArgsConstructor
public class JiacService {

    private final IssueDataProvider dataProvider;
    private final SchemaParser issueParser = new SchemaParser();
    private final IssueComparator issueComparator = new IssueComparator();

    public Issue parseFromString(String value) throws SchemaParsingError {
        return issueParser.parseFromString(value);
    }

    public Issue parseFromFile(String value) throws SchemaParsingError {
        return issueParser.parseFromFile(value);
    }

    public Issue getIssueWithChildren(String key) {
        return dataProvider.getWithChildren(key);
    }

    public String getSchemaForIssueWithChildren(String key) {
        return SchemaWriter.toString(getIssueWithChildren(key));
    }

    public List<Difference<?>> diff(String key, String schema) throws Exception {
        Issue from = dataProvider.getWithChildren(key);
        Issue to = issueParser.parseFromString(schema);
        return diff(from, to);
    }

    public List<Difference<?>> diff(Issue from, Issue to) throws Exception {
        return issueComparator.compare(from, to);
    }
}
