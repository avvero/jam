package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pw.avvero.jam.schema.Issue;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class DifferenceNewSubTask extends Difference {

    private final Issue parent;
    private final Issue child;

}
