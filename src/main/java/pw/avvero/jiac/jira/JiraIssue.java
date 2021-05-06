package pw.avvero.jiac.jira;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class JiraIssue implements Serializable {

    private int id;
    private String key;
    private Fields fields;

}
