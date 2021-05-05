package pw.avvero.jiac;

import pw.avvero.jiac.entity.Issue;

import java.util.List;

public class IssueApiDataProvider extends IssueDataProvider{

    @Override
    public Issue getByCode(String key) {
        return null;
    }

    @Override
    protected List<Issue> getIssuesInEpic(String key) {
        return null;
    }
}
