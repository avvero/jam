package pw.avvero.jam.jira;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {

    private int total;
    private List<JiraIssue> issues;

}
