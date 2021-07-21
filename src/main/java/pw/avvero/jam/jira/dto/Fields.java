package pw.avvero.jam.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fields implements Serializable {

    private IssueType issuetype;
    private Project project;
    private String summary;
    private JiraIssue parent;
    private List<JiraIssue> subtasks;
    private List<JiraIssueLink> issuelinks;

}