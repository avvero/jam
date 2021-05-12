package pw.avvero.jam.schema;

import pw.avvero.jam.jira.JiraIssue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class JiraIssueMapper {

    public static Issue map(JiraIssue jiraIssue, Issue parent) {
        String key = jiraIssue.getKey();
        String project = jiraIssue.getFields().getProject() != null ? jiraIssue.getFields().getProject().getKey() :
                getProject(key);
        String type = jiraIssue.getFields().getIssuetype().getName();
        String summary = jiraIssue.getFields().getSummary();
        Issue issue = new Issue(project, key, type, summary, parent, emptyList());
        List<Issue> children = Optional.ofNullable(jiraIssue.getFields().getSubtasks()).orElse(emptyList()).stream()
                .map(c -> map(c, issue))
                .collect(Collectors.toList());
        issue.setChildren(children);
        return issue;
    }

    /**
     * Get project from key
     * @param key
     * @return
     */
    private static String getProject(String key) {
        return key.split("-")[0];
    }

}
