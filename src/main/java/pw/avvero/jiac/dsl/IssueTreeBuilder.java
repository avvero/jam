package pw.avvero.jiac.dsl;

import java.util.Iterator;
import java.util.List;

public class IssueTreeBuilder {

    public static Issue build(List<LeveledIssue> list) {
        if (list == null || list.isEmpty()) return null;

        Iterator<LeveledIssue> iterator = list.iterator();
        LeveledIssue root = iterator.next();
        LeveledIssue ignored = walk(root, iterator);
        return root.getIssue();
    }

    private static LeveledIssue walk(LeveledIssue previous, Iterator<LeveledIssue> iterator) {
        while (iterator.hasNext()) {
            LeveledIssue current = iterator.next();
            if (current.getLevel() > previous.getLevel()) {
                previous.getIssue().getChildren().add(current.getIssue());
            } else {
                return current;
            }
            LeveledIssue next = walk(current, iterator);
            if (next == null) return current;
            if (next.getLevel() == current.getLevel()) {
                previous.getIssue().getChildren().add(next.getIssue());
            } else {
                return next;
            }
        }
        return null;
    }

}
