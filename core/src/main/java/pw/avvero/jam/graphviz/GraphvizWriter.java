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
        return toString(issue, null);
    }

    public static String toString(Issue issue, String fromIssueCode) {
        StringBuilder sb = new StringBuilder("digraph jam {\n");
        sb.append("    graph [nodesep=\"0.1\"];\n");
        sb.append("    rankdir=LR;\n");
        sb.append("    node [shape=box, penwidth=1];\n");
        printNodes(sb, issue);
        printEdges(sb, null, issue);
        if (fromIssueCode != null) {
            String fromIssueLabel = "Back to " + fromIssueCode;
            sb.append(format("    \"%s\" [shape=lpromoter, URL=\"dependencies?issueCode=%s\"];\n", fromIssueLabel,
                    fromIssueCode));
            sb.append(format("    \"%s\" -> \"%s\" [style=dashed];\n", fromIssueLabel, issue.getKey()));
        }
        sb.append("}");
        return sb.toString();
    }

    private static void printNodes(StringBuilder sb, Issue issue) {
        Map<String, Issue> issues = new HashMap<>();
        putIssues(issues, issue);
        for (Issue entry: issues.values()) {
            if (entry.getKey().startsWith("STAT-")) continue;
            if (isIgnored(entry.getType())) continue;

            sb.append(node(entry, issue));
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

                sb.append(edge(issue, issueLink));
            }
        }
    }

    private static String node(Issue issue, Issue root) {
        String color;
        switch (issue.getStatus()) {
            case "new": color = "green"; break;
            case "done": color = "grey"; break;
            case "indeterminate": color = "blue"; break;
            default: color = "black";
        }
        String label = format("<%s<BR/><FONT POINT-SIZE=\"10\">%s</FONT>>", issue.getKey(), escape(issue.getSummary()));
        String url = format("dependencies?issueCode=%s&from=%s", issue.getKey(), root.getKey());
        return format("    \"%s\" [shape=box, color=%s, label=%s, URL=\"%s\"];\n", issue.getKey(), color,
                label, url);
    }

    private static String escape(String summary) {
        if (summary == null) return null;
        return summary
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    private static String edge(Issue issue, IssueLink issueLink) {
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
