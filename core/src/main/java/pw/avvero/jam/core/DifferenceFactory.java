package pw.avvero.jam.core;

public class DifferenceFactory {

    public static DifferenceSummary ofSummary(Issue from, Issue to) {
        return new DifferenceSummary(from.getKey(), from.getSummary(), to.getSummary());
    }

    public static DifferenceNewIssueInEpic ofNewIssueInEpic(Issue epic, Issue issue) {
        return new DifferenceNewIssueInEpic(epic, issue);
    }

    public static DifferenceNewSubTask ofNewSubTask(Issue parent, Issue child) {
        return new DifferenceNewSubTask(parent, child);
    }

    public static DifferenceMoveSubTaskToParent ofChangeParent(Issue parent, Issue child) {
        return new DifferenceMoveSubTaskToParent(parent, child);
    }

}
