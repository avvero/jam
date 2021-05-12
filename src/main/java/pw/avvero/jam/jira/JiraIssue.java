package pw.avvero.jam.jira;

import lombok.Data;

import java.io.Serializable;

@Data
public class JiraIssue implements Serializable {

    private int id;
    private String key;
    private Fields fields;

}
