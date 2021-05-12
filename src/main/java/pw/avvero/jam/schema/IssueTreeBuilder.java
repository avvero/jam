package pw.avvero.jam.schema;

import java.util.Iterator;
import java.util.List;

public class IssueTreeBuilder {

    public static Issue build(List<LeveledIssue> list) {
        if (list == null || list.isEmpty()) return null;

        Iterator<LeveledIssue> iterator = list.iterator();
        LeveledIssue root = next(iterator);
        LeveledIssue ignored = walk(root, next(iterator), iterator);
        return root.getIssue();
    }

    private static LeveledIssue walk(LeveledIssue previous, LeveledIssue current, Iterator<LeveledIssue> iterator) {
        if (current == null) return null;
        if (current.getLevel() > previous.getLevel()) {
            previous.getIssue().getChildren().add(current.getIssue());
            LeveledIssue next = walk(current, next(iterator), iterator);
            if (next != null && next.getLevel() == current.getLevel()) {
                return walk(previous, next, iterator);
            }
            return next;
        }
        return current;
    }

    private static LeveledIssue next(Iterator<LeveledIssue> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
