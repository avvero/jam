package pw.avvero.jiac.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    private String project;
    private String key;
    private String type;
    private String summary;
    private List<Issue> children = new ArrayList<>();

}
