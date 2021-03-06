package pw.avvero.jam.jira;

import pw.avvero.jam.core.Issue;
import pw.avvero.jam.core.IssueLink;
import pw.avvero.jam.jira.dto.JiraIssue;
import pw.avvero.jam.jira.dto.JiraIssueLink;

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
        String status = jiraIssue.getFields().getStatus().getStatusCategory().getKey();
        Issue issue = new Issue(project, key, type, summary, status, parent, emptyList(), emptyList());
        List<Issue> children = Optional.ofNullable(jiraIssue.getFields().getSubtasks()).orElse(emptyList()).stream()
                .map(c -> map(c, issue))
                .collect(Collectors.toList());
        issue.setChildren(children);
        List<IssueLink> links = Optional.ofNullable(jiraIssue.getFields().getIssuelinks()).orElse(emptyList()).stream()
                .map(JiraIssueMapper::map)
                .collect(Collectors.toList());
        issue.setLinks(links);
        return issue;
    }

    public static IssueLink map(JiraIssueLink jiraIssueLink) {
        IssueLink issueLink = new IssueLink();
        if (jiraIssueLink.getInwardIssue() != null) {
            issueLink.setType(jiraIssueLink.getType().getInward());
            issueLink.setIssue(map(jiraIssueLink.getInwardIssue(), null));
        }
        if (jiraIssueLink.getOutwardIssue() != null) {
            issueLink.setType(jiraIssueLink.getType().getOutward());
            issueLink.setIssue(map(jiraIssueLink.getOutwardIssue(), null));
        }
        return issueLink;
    }

    /**
     * Get project from key
     *
     * @param key
     * @return
     */
    private static String getProject(String key) {
        return key.split("-")[0];
    }

}
