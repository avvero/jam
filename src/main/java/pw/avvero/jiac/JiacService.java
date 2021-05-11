package pw.avvero.jiac;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import pw.avvero.jiac.schema.Issue;
import pw.avvero.jiac.schema.SchemaParser;
import pw.avvero.jiac.schema.SchemaWriter;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JiacService {

    private final IssueDataProvider dataProvider;
    private final SchemaParser issueParser;
    private final IssueComparator issueComparator = new IssueComparator();

    public Issue getIssueWithChildren(String key) {
        return dataProvider.getWithChildren(key);
    }

    public String getSchemaForIssueWithChildren(String key) {
        return SchemaWriter.toString(getIssueWithChildren(key));
    }

    public <T> List<Difference<T>> diff(String key, String schema) throws Exception {
        Issue from = dataProvider.getWithChildren(key);
        Issue to = issueParser.parseFromString(schema);
        return issueComparator.compare(from, to);
    }
}
