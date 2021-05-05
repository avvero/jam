package pw.avvero.jiac.jira;

import lombok.Data;

import java.io.Serializable;

@Data
public class JiraIssue implements Serializable {

    private String key;
    private Fields fields;

}
