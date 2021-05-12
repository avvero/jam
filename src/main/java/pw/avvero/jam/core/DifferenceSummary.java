package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DifferenceSummary implements Difference {

    private final String issueKey;
    private final String oldValue;
    private final String newValue;

}
