package pw.avvero.jiac.dsl;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Issue {

    private String project;
    private String key;
    private String type;
    private String summary;
    private List<Issue> children = new ArrayList<>();

}
