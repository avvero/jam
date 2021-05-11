package pw.avvero.jiac.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import pw.avvero.jiac.schema.Issue;

@Data
@AllArgsConstructor
public class Difference<T> {

    public static final String SUMMARY_CHANGED = "summary is changed";
    public static final String ISSUE_ABSENT = "summary is absent";
    public static final String NEW_ISSUE = "new issue";

    private final Issue issue;
    private final String type;
    private final T oldValue;
    private final T newValue;

}
