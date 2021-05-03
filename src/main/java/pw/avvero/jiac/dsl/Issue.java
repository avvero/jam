package pw.avvero.jiac.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Issue {

    private String project;
    private String type;
    private String summary;
    private List<Issue> children = new ArrayList<>();

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Issue> getChildren() {
        return children;
    }

    public void setChildren(List<Issue> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "project='" + project + '\'' +
                ", type='" + type + '\'' +
                ", summary='" + summary + '\'' +
                ", children=" + children +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(project, issue.project) && Objects.equals(type, issue.type) && Objects.equals(summary, issue.summary) && Objects.equals(children, issue.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, type, summary, children);
    }
}
