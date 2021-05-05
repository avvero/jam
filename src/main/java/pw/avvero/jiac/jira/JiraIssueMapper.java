package pw.avvero.jiac.jira;

import pw.avvero.jiac.entity.Issue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class JiraIssueMapper {

    public static Issue map(JiraIssue jiraIssue) {
        String key = jiraIssue.getKey();
        String project = jiraIssue.getFields().getProject() != null ? jiraIssue.getFields().getProject().getKey() :
                getProject(key);
        String type = jiraIssue.getFields().getIssuetype().getName();
        String summary = jiraIssue.getFields().getSummary();
        Issue issue = new Issue(project, key, type, summary, emptyList());
        List<Issue> children = Optional.ofNullable(jiraIssue.getFields().getSubtasks()).orElse(emptyList()).stream()
                .map(JiraIssueMapper::map)
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
