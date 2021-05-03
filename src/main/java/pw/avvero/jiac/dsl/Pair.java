package pw.avvero.jiac.dsl;

public class Pair {

    private int level;
    private Issue issue;

    public Pair(int level, Issue issue) {
        this.level = level;
        this.issue = issue;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "{" +
                "level=" + level +
                ", issue=" + issue +
                '}';
    }
}
