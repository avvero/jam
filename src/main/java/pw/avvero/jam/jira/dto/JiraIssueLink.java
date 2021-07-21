package pw.avvero.jam.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraIssueLink implements Serializable {

    private IssueLinkType type;
    private JiraIssue inwardIssue;
    private JiraIssue outwardIssue;

}
