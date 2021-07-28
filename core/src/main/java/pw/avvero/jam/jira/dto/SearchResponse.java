package pw.avvero.jam.jira.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchResponse {

    private int total;
    private List<JiraIssue> issues;

}
