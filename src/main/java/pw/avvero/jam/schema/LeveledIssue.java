package pw.avvero.jam.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeveledIssue {

    private int level;
    private Issue issue;

}
