package pw.avvero.jiac;

import lombok.AllArgsConstructor;
import pw.avvero.jiac.schema.Issue;

@AllArgsConstructor
public class JiacService {

    private final IssueDataProvider dataProvider;

    public Issue getIssueWithChildren(String key) {
        return dataProvider.getWithChildren(key);
    }

    public String getSchemaForIssue(String key) {
        return null;
    }

}
