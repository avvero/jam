package pw.avvero.jam.jira;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Fields implements Serializable {

    private IssueType issuetype;
    private Project project;
    private String summary;
    private List<JiraIssue> subtasks;

}
