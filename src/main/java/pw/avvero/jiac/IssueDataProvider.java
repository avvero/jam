package pw.avvero.jiac;

import pw.avvero.jiac.entity.Issue;

import java.util.List;

public abstract class IssueDataProvider {

    public Issue getWithChildren(String key) {
        Issue root = getByCode(key);
        if ("Epic".equalsIgnoreCase(root.getType())) {
            List<Issue> epiclings = getIssuesInEpic(key);
            if (epiclings != null && !epiclings.isEmpty()) {
                root.getChildren().addAll(epiclings);
            }
        }
        return root;
    }

    public abstract Issue getByCode(String key);

    protected abstract List<Issue> getIssuesInEpic(String key);
}
