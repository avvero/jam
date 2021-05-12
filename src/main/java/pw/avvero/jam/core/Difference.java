package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import pw.avvero.jam.schema.Issue;

@Data
@AllArgsConstructor
@ToString
public class Difference<T> {

    public static final String SUMMARY_CHANGED = "summary is changed";
    public static final String ISSUE_ABSENT = "summary is absent";
    public static final String NEW_ISSUE = "new issue";

    private final String issueKey;
    private final String type;
    private final T oldValue;
    private final T newValue;

    public static Difference<String> ofSummary(Issue from, Issue to) {
        return new Difference<>(from.getKey(), SUMMARY_CHANGED, from.getSummary(), to.getSummary());
    }

}
