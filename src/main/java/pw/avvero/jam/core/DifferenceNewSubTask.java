package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import pw.avvero.jam.schema.Issue;

@Data
@AllArgsConstructor
public class DifferenceNewSubTask extends Difference {

    private final Issue parent;
    private final Issue child;

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
