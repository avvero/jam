package pw.avvero.jam;

import pw.avvero.jam.core.IssueDataProvider;
import pw.avvero.jam.schema.Issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueMapDataProvider extends IssueDataProvider {

    private final Map<String, Issue> issues = new HashMap<>();

    @Override
    public Issue getByCode(String key) {
        return issues.get(key);
    }

    @Override
    protected List<Issue> getIssuesInEpic(String key, Issue epic) {
        return new ArrayList<>();
    }

    public void put(String key, Issue issue) {
        issues.put(key, issue);
    }
}
