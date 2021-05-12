package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DifferenceSummary extends Difference {

    private final String issueKey;
    private final String oldValue;
    private final String newValue;

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
