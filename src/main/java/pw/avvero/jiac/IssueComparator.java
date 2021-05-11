package pw.avvero.jiac;

import pw.avvero.jiac.schema.Issue;

import java.util.ArrayList;
import java.util.List;

public class IssueComparator {
    public <T> List<Difference<T>> compare(Issue from, Issue to) {
        return new ArrayList<>();
    }
}
