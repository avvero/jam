package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DifferenceNewIssueInEpic implements Difference {

    private final Issue epic;
    private final Issue issue;

}
