package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import pw.avvero.jam.schema.Issue;

@Data
@AllArgsConstructor
@ToString
public abstract class Difference {

    public static final String SUMMARY_CHANGED = DifferenceSummary.class.getSimpleName();
    public static final String ISSUE_ABSENT = "summary is absent";
    public static final String NEW_ISSUE = DifferenceNewIssue.class.getSimpleName();
    public static final String NEW_ISSUE_IN_EPIC = DifferenceNewIssueInEpic.class.getSimpleName();
    public static final String NEW_SUB_TASK = DifferenceNewSubTask.class.getSimpleName();

    public abstract String getType();

    public static DifferenceSummary ofSummary(Issue from, Issue to) {
        return new DifferenceSummary(from.getKey(), from.getSummary(), to.getSummary());
    }

    public static DifferenceNewIssue ofNewIssue(Issue to) {
        return new DifferenceNewIssue(to);
    }

    public static DifferenceNewIssueInEpic ofNewIssueInEpic(Issue epic, Issue issue) {
        return new DifferenceNewIssueInEpic(epic, issue);
    }

    public static DifferenceNewSubTask ofNewSubTask(Issue parent, Issue child) {
        return new DifferenceNewSubTask(parent, child);
    }

}
