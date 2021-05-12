package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.avvero.jam.schema.Issue;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class DifferenceNewIssue extends Difference {

    private final Issue issue;

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
