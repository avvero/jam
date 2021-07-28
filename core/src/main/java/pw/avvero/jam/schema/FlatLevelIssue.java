package pw.avvero.jam.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pw.avvero.jam.core.Issue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlatLevelIssue {

    private int level;
    private Issue issue;

}
