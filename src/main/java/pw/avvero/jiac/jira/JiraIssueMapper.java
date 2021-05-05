package pw.avvero.jiac.jira;

import pw.avvero.jiac.entity.Issue;

import static java.util.Collections.emptyList;

public class JiraIssueMapper {

    public static Issue map(JiraIssue jiraIssue) {
        String project = jiraIssue.getFields().getProject().getKey();
        String key = jiraIssue.getKey();
        String type = jiraIssue.getFields().getIssuetype().getName();
        String summary = jiraIssue.getFields().getSummary();
        return new Issue(project, key, type, summary, emptyList());
    }

}
