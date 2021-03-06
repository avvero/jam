package pw.avvero.jam.core;

import pw.avvero.jam.JamException;

import java.util.List;

public abstract class IssueDataProvider {

    /**
     * Returns issues with children:
     * - sub-tasks any kind if issue has it
     * - task of ony kind withing epic if this is this epic
     *
     * @param key
     * @return
     */
    public Issue getWithChildren(String key) {
        Issue root = getByCode(key);
        if (root == null) return null;
        if ("Epic".equalsIgnoreCase(root.getType())) {
            List<Issue> epiclings = getIssuesInEpic(key, root);
            if (epiclings != null && !epiclings.isEmpty()) {
                root.getChildren().addAll(epiclings);
            }
        }
        return root;
    }

    public abstract Issue getByCode(String key);

    protected abstract List<Issue> getIssuesInEpic(String key, Issue epic);

    public abstract void updateSummary(String key, String newValue);

    /**
     * Adds sub task for the issue
     *
     * @param parent
     * @param child
     * @return key for new issue
     */
    public abstract String addSubTask(Issue parent, Issue child) throws JamException;

    public abstract void addIssueToEpic(Issue epic, Issue issue);

    public abstract void moveSubTaskToParent(Issue parent, Issue child) throws JamException;
}
