package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class DifferenceSummary extends Difference {

    private final String issueKey;
    private final String oldValue;
    private final String newValue;

}
