package pw.avvero.jam.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DifferenceSubTaskChangesParent implements Difference {

    private final Issue parent;
    private final Issue child;

}
