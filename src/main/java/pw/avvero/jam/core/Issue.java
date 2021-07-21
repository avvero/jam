package pw.avvero.jam.core;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
public class Issue {

    private String project;
    private String key;
    private String type;
    private String summary;
    private Issue parent;
    private List<Issue> children = new ArrayList<>();
    private List<IssueLink> links = new ArrayList<>();

}
