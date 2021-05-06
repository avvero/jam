package pw.avvero.jiac.schema;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

public class SchemaWriter {

    /**
     * Writes issue to schema
     * @param issue
     * @return
     */
    public static String toString(Issue issue) {
        return toString(0, issue);
    }

    private static String toString(int level, Issue issue) {
        String key = issue.getKey() != null ? issue.getKey() : issue.getProject();
        StringBuilder sb = new StringBuilder();
        sb.append(format("%s [%s:%s] %s", levelPrefix(level), key, issue.getType(), issue.getSummary()));
        if (issue.getChildren() != null && !issue.getChildren().isEmpty()) {
            String children = Optional.ofNullable(issue.getChildren()).orElse(emptyList()).stream()
                    .map(child -> toString(level + 1, child))
                    .collect(Collectors.joining("\n"));
            sb.append("\n").append(children);
        }
        return sb.toString();
    }

    private static String levelPrefix(int level) {
        if (level == 0) return "#";
        return Stream.generate(() -> "-").limit(level).collect(Collectors.joining(" "));
    }

}
