package pw.avvero.jiac;

import lombok.AllArgsConstructor;
import pw.avvero.jiac.schema.Issue;
import pw.avvero.jiac.schema.SchemaWriter;

@AllArgsConstructor
public class JiacService {

    private final IssueDataProvider dataProvider;

    public Issue getIssueWithChildren(String key) {
        return dataProvider.getWithChildren(key);
    }

    public String getSchemaForIssueWithChildren(String key) {
        return SchemaWriter.toString(getIssueWithChildren(key));
    }

}
