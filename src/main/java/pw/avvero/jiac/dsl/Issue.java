package pw.avvero.jiac.dsl;

public class Issue {

    private String project;
    private String type;
    private String summary;

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

    @Override
    public String toString() {
        return "Issue{" +
                "project='" + project + '\'' +
                ", type='" + type + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
