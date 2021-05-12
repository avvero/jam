package pw.avvero.jam.core;

import lombok.RequiredArgsConstructor;
import pw.avvero.jam.schema.Issue;
import pw.avvero.jam.schema.SchemaParser;
import pw.avvero.jam.schema.SchemaParsingError;
import pw.avvero.jam.schema.SchemaWriter;

import java.util.List;

@RequiredArgsConstructor
public class JamService {

    private final IssueDataProvider dataProvider;
    private final DifferenceResolver differenceResolver;
    private final SchemaParser issueParser = new SchemaParser();
    private final IssueComparator issueComparator = new IssueComparator();

    public JamService(IssueDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.differenceResolver = new DifferenceResolver(dataProvider);
    }

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

    public List<Difference> diff(String key, String schema) throws SchemaParsingError, IssueComparisonException {
        Issue from = dataProvider.getWithChildren(key);
        Issue to = issueParser.parseFromString(schema);
        return diff(from, to);
    }

    public List<Difference> diff(Issue from, Issue to) throws SchemaParsingError, IssueComparisonException {
        return issueComparator.compare(from, to);
    }

    public void offer(String schema) throws SchemaParsingError, IssueComparisonException {
        Issue to = parseFromString(schema);
        Issue from = getIssueWithChildren(to.getKey());
        List<Difference> diffs = diff(from, to);
        differenceResolver.resolve(diffs);
    }
}
