package pw.avvero.jam.graphviz;

import pw.avvero.jam.core.Issue;
import pw.avvero.jam.core.IssueLink;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class GraphvizWriter {

    public static String toString(Issue issue) {
        StringBuilder sb = new StringBuilder("digraph jam {\n");
        sb.append("    graph [nodesep=\"0.1\"];\n");
        sb.append("    rankdir=LR;\n");
        sb.append("    node [shape=box];\n");
        printNodes(sb, issue);
        printEdges(sb, null, issue);
        sb.append("}");
        return sb.toString();
    }

    private static void printNodes(StringBuilder sb, Issue issue) {
        Map<String, Issue> issues = new HashMap<>();
        putIssues(issues, issue);
        for (Issue entry: issues.values()) {
            if (entry.getKey().startsWith("STAT-")) continue;
            if (isIgnored(entry.getType())) continue;

            String color;
            switch (entry.getStatus()) {
                case "new": color = "green"; break;
                case "done": color = "grey"; break;
                case "indeterminate": color = "blue"; break;
                default: color = "black";
            }
            sb.append(format("    \"%s\" [shape=box, color=%s];\n", entry.getKey(), color));
//            sb.append(format("    \"%s\" [shape=box, color=%s, label=\"%s\\l%s\"];\n", entry.getKey(), color, entry.getKey(), entry.getSummary()));
        }
    }

    private static void putIssues(Map<String, Issue> issues, Issue issue) {
        issues.putIfAbsent(issue.getKey(), issue);
        if (issue.getChildren() != null) {
            for (Issue child: issue.getChildren()) {
                putIssues(issues, child);
            }
        }
        if (issue.getLinks() != null) {
            for (IssueLink link: issue.getLinks()) {
                putIssues(issues, link.getIssue());
            }
        }
    }

    private static void printEdges(StringBuilder sb, Issue parent, Issue issue) {
        if (parent != null) {
            sb.append(format("    \"%s\" -> \"%s\" [color=grey];\n", parent.getKey(), issue.getKey()));
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

                printEdges(sb, issue, child);
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
        String label = issueLink.getType();
        switch (issueLink.getType()) {
            case "depends on": color = "orange"; break;
            case "is blocked by": color = "orange"; break;

            case "relates to": color = "green"; break;
            case "is related to": color = "green"; break;

            case "enables": color = "red"; break;
            case "blocks": color = "red"; break;

            default: color = "black"; break;
        }

        if ("done".equals(issue.getStatus())) {
            color = "grey";
            label = "";
        }

        return format("    \"%s\" -> \"%s\" [style=dashed, label=\"%s\", color=%s];\n",
                issue.getKey(),
                issueLink.getIssue().getKey(),
                label,
                color);
    }

    private static boolean isIgnored(String type) {
        return Arrays.asList("Sub-Task", "Sub-Bug").contains(type);
    }
}
