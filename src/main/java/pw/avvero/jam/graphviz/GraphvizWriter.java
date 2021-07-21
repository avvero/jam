package pw.avvero.jam.graphviz;

import pw.avvero.jam.core.Issue;

import java.util.stream.Collectors;

import static java.lang.String.format;

public class GraphvizWriter {

    public static String toString(Issue issue) {
        StringBuilder sb = new StringBuilder("digraph jam {\n");
        toString(sb, null, issue);
        sb.append("}");
        return sb.toString();
    }

    private static void toString(StringBuilder sb, Issue parent, Issue issue) {
        if (parent != null) {
            sb.append(format("    \"%s\" -> \"%s\";\n", parent.getKey(), issue.getKey()));
        } else {
            sb.append(format("    \"%s\";\n", issue.getKey()));
        }
        if (issue.getChildren() != null && !issue.getChildren().isEmpty()) {
            // Rank
            String rank = issue.getChildren().stream()
                    .map(i -> format("\"%s\"", i.getKey()))
                    .collect(Collectors.joining(" ", "{ rank=same ", "}\n"));
            sb.append("    ").append(rank);
            // Children
            for (Issue child : issue.getChildren()) {
                toString(sb, issue, child);
            }
        }
    }
}
