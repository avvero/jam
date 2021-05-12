package pw.avvero.jam.schema;

import java.util.Iterator;
import java.util.List;

public class IssueTreeBuilder {

    public static Issue build(List<FlatLevelIssue> list) {
        if (list == null || list.isEmpty()) return null;

        Iterator<FlatLevelIssue> iterator = list.iterator();
        FlatLevelIssue root = next(iterator);
        FlatLevelIssue ignored = walk(root, next(iterator), iterator);
        return root.getIssue();
    }

    private static FlatLevelIssue walk(FlatLevelIssue previous, FlatLevelIssue current, Iterator<FlatLevelIssue> iterator) {
        if (current == null) return null;
        if (current.getLevel() > previous.getLevel()) {
            Issue parent = previous.getIssue();
            Issue child = current.getIssue();
            parent.getChildren().add(child);
            child.setParent(parent);
            FlatLevelIssue next = walk(current, next(iterator), iterator);
            if (next != null && next.getLevel() == current.getLevel()) {
                return walk(previous, next, iterator);
            }
            return next;
        }
        return current;
    }

    private static FlatLevelIssue next(Iterator<FlatLevelIssue> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
