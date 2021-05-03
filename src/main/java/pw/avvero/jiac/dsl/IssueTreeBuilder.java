package pw.avvero.jiac.dsl;

import java.util.List;

public class IssueTreeBuilder {

    public static Issue build(List<Pair<Integer, Issue>> list) {
        if (list == null || list.isEmpty()) return null;
        Pair<Integer, Issue> root = null;
        Pair<Integer, Issue> previous = null;
        for (Pair<Integer, Issue> current : list) {
            if (root == null) {
                root = current;
                previous = current;
                continue;
            }
//            if

            previous = current;
        }
        return previous.getV();
    }

    private static void tree(Issue parent, List<Issue> children) {

    }

}
