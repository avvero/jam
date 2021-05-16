package pw.avvero.jam;

import pw.avvero.jam.core.IssueDataProvider;
import pw.avvero.jam.core.Issue;

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

    @Override
    public void updateSummary(String key, String newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String addSubTask(Issue parent, Issue child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addIssueToEpic(Issue epic, Issue issue) {
        throw new UnsupportedOperationException();
    }
}
