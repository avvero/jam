package pw.avvero.jiac.core;

import pw.avvero.jiac.schema.Issue;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static pw.avvero.jiac.core.Difference.SUMMARY_CHANGED;

public class IssueComparator {

    public <T> List<Difference<T>> compare(Issue from, Issue to) throws IssueComparisonException {
        if (from == null || to == null)
            throw new IssueComparisonException("Can't compare empty issues");
        if (isBlank(from.getKey()) || isBlank(to.getKey()))
            throw new IssueComparisonException("Can't compare keyless issues");
        if (!from.getKey().equals(to.getKey()))
            throw new IssueComparisonException("Issues has different keys");

        List<Difference<T>> differences = new ArrayList<>();
        if (!from.getSummary().trim().equals(to.getSummary().trim())) {
            differences.add(new Difference(from.getKey(), SUMMARY_CHANGED, from.getSummary(), to.getSummary()));
        }

        return differences;
    }
}
