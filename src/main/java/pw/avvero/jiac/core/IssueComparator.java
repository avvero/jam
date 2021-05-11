package pw.avvero.jiac.core;

import pw.avvero.jiac.schema.Issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IssueComparator {
    public <T> List<Difference<T>> compare(Issue from, Issue to) {
        if (from == null || to == null) return Collections.emptyList();

        return new ArrayList<>();
    }
}
