package pw.avvero.jam.core;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class IssueComparator {

    public List<Difference> compare(Issue from, Issue to) throws IssueComparisonException {
        List<Difference> diffs = new ArrayList<>();
        compare(diffs, from, to);
        return diffs;
    }

    public void compare(List<Difference> diffs, Issue from, Issue to) throws IssueComparisonException {
        if (from == null || to == null)
            throw new IssueComparisonException("Can't compare empty issues");
        if (isBlank(from.getKey()) || isBlank(to.getKey()))
            throw new IssueComparisonException("Can't compare keyless issues");
        if (!from.getKey().equals(to.getKey()))
            throw new IssueComparisonException("Issues has different keys");

        // Summary
        if (!isSummaryEqual(from, to)) {
            diffs.add(DifferenceFactory.ofSummary(from, to));
        }
        Map<String, Issue> fromIssuesMap = new HashMap<>();
        alignToMap(fromIssuesMap, from);
        // Compare children
        if (to.getChildren() != null) {
            for (Issue toChild : to.getChildren()) {
                if (StringUtils.isBlank(toChild.getKey())) {
                    if ("Epic".equals(to.getType())) {
                        diffs.add(DifferenceFactory.ofNewIssueInEpic(to, toChild));
                    } else {
                        diffs.add(DifferenceFactory.ofNewSubTask(to, toChild));
                    }
                } else {
                    Issue fromChild = fromIssuesMap.get(toChild.getKey());
                    if (fromChild != null) {
                        compare(diffs, fromChild, toChild);
                    } else {
                        // Child with key is not presented in current parent, but is presented in offer
                        diffs.add(DifferenceFactory.ofChangeParent(to, toChild));
                    }
                }
            }
        }
    }

    private void alignToMap(Map<String, Issue> map, Issue issue) {
        if (issue == null) return;
        map.put(issue.getKey(), issue);

        Optional.ofNullable(issue.getChildren()).orElse(emptyList())
                .forEach(i -> alignToMap(map, i));
    }

    private boolean isSummaryEqual(Issue from, Issue to) {
        return from.getSummary().trim().equals(to.getSummary().trim());
    }
}
