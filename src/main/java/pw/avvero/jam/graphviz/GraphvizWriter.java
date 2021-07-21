package pw.avvero.jam.graphviz;

import pw.avvero.jam.core.Issue;
import pw.avvero.jam.core.IssueLink;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class GraphvizWriter {

    public static String toString(Issue issue) {
        StringBuilder sb = new StringBuilder("digraph jam {\n");
        sb.append("    rankdir=LR;\n");
        sb.append("    node [shape=box];\n");
        toString(sb, null, issue);
        sb.append("}");
        return sb.toString();
    }

    private static void toString(StringBuilder sb, Issue parent, Issue issue) {
        if (parent != null) {
            sb.append(format("    \"%s\" -> \"%s\";\n", parent.getKey(), issue.getKey()));
        } else {
            sb.append(format("    \"%s\" [shape=Mdiamond];\n", issue.getKey()));
        }
        // Children
        if (issue.getChildren() != null && !issue.getChildren().isEmpty()) {
            // Rank
            String rank = issue.getChildren().stream()
                    .map(i -> format("\"%s\"", i.getKey()))
                    .collect(Collectors.joining(" ", "{ rank=same ", "}\n"));
//            sb.append("    ").append(rank);
            for (Issue child : issue.getChildren()) {
                if (child.getKey().startsWith("STAT-")) continue;
                if (isIgnored(child.getType())) continue;

                toString(sb, issue, child);
            }
        }
        // Links
        if (issue.getLinks() != null && issue.getLinks().size() > 0) {
            for (IssueLink issueLink : issue.getLinks()) {
                if (issueLink.getIssue().getKey().startsWith("STAT-")) continue;
                if (isIgnored(issueLink.getIssue().getType())) continue;

                sb.append(link(issue, issueLink));
            }
        }
    }

    private static String link(Issue issue, IssueLink issueLink) {
        String color;
        switch (issueLink.getType()) {
            case "depends on": color = "orange"; break;
            case "is blocked by": color = "orange"; break;

            case "relates to": color = "green"; break;
            case "is related to": color = "green"; break;

            case "enables": color = "red"; break;
            case "blocks": color = "red"; break;

            default: color = "black"; break;
        }

        return format("    \"%s\" -> \"%s\" [style=dashed, label=\"%s\", color=%s];\n",
                issue.getKey(),
                issueLink.getIssue().getKey(),
                issueLink.getType(),
                color);
    }

    private static boolean isIgnored(String type) {
        return Arrays.asList("Sub-Task", "Sub-Bug").contains(type);
    }
}
