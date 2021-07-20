package pw.avvero.jam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pw.avvero.jam.core.*;
import pw.avvero.jam.schema.SchemaParser;
import pw.avvero.jam.schema.SchemaParsingError;
import pw.avvero.jam.schema.SchemaWriter;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JamService {

    private final IssueDataProvider dataProvider;
    private final DifferenceResolver differenceResolver;
    private final SchemaParser issueParser;
    private final IssueComparator issueComparator;

    public JamService(IssueDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.differenceResolver = new DifferenceResolver(dataProvider);
        this.issueParser = new SchemaParser();
        this.issueComparator = new IssueComparator();
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

    /**
     * Checkouts issue by key and returns schema representation as string
     * @param issueKey
     * @return
     */
    public String checkout(String issueKey) {
        Issue issue = getIssueWithChildren(issueKey);
        return SchemaWriter.toString(issue);
    }

    public String offer(String schema) throws SchemaParsingError, IssueComparisonException, JamException {
        Issue to = parseFromString(schema);
        if (StringUtils.isBlank(to.getKey())) {
            throw new JamException("Please provide key for root issue");
        }
        offer(to);
        Issue issue = getIssueWithChildren(to.getKey());
        return SchemaWriter.toString(issue);
    }

    public void offer(Issue to) throws SchemaParsingError, IssueComparisonException, JamException {
        Issue from = getIssueWithChildren(to.getKey());
        List<Difference> diffs = diff(from, to);
        if (diffs == null || diffs.size() == 0) {
            log.info("There are no difference");
        } else {
            log.info("There differences: ");
            diffs.forEach(d -> log.info("    [" + d.toString() + "]"));
            differenceResolver.resolve(diffs);
        }
    }
}
