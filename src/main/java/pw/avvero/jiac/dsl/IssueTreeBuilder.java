package pw.avvero.jiac.dsl;

import java.util.Iterator;
import java.util.List;

public class IssueTreeBuilder {

    public static Issue build(List<Pair> list) {
        if (list == null || list.isEmpty()) return null;

        Iterator<Pair> iterator = list.iterator();
        Pair root = iterator.next();
        Pair ignored = walk(root, iterator);
        return root.getIssue();
    }

    private static Pair walk(Pair previous, Iterator<Pair> iterator) {
        while (iterator.hasNext()) {
            Pair current = iterator.next();
            if (current.getLevel() > previous.getLevel()) {
                previous.getIssue().getChildren().add(current.getIssue());
            } else {
                return current;
            }
            Pair next = walk(current, iterator);
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
