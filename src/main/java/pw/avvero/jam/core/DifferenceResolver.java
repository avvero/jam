package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DifferenceResolver {

    private final IssueDataProvider dataProvider;

    public void resolve(List<Difference> differences) {
        for (Difference difference : differences) {
            if (difference instanceof DifferenceSummary) {
                DifferenceSummary diff = (DifferenceSummary) difference;
                dataProvider.updateSummary(diff.getIssueKey(), diff.getNewValue());
                continue;
            }
            if (difference instanceof DifferenceNewSubTask) {
                DifferenceNewSubTask diff = (DifferenceNewSubTask) difference;
                String key = dataProvider.addSubTask(diff.getParent(), diff.getChild());
                diff.getChild().setKey(key);
                continue;
            }
            if (difference instanceof DifferenceNewIssueInEpic) {
                DifferenceNewIssueInEpic diff = (DifferenceNewIssueInEpic) difference;
                dataProvider.addIssueToEpic(diff.getEpic(), diff.getIssue());
                continue;
            }
            throw new UnsupportedOperationException("Difference with type is not supported: "
                    + difference.getClass().getSimpleName());
        }
    }

}
