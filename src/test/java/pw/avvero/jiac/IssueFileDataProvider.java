package pw.avvero.jiac;

import lombok.AllArgsConstructor;
import pw.avvero.jiac.schema.Issue;
import pw.avvero.jiac.jira.JiraIssue;
import pw.avvero.jiac.schema.JiraIssueMapper;
import pw.avvero.jiac.jira.SearchResponse;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static pw.avvero.test.ResourceDataProvider.fromFile;

@AllArgsConstructor
public class IssueFileDataProvider extends IssueDataProvider {

    private final String path;

    @Override
    public Issue getByCode(String key) {
        try {
            String content = fromFile(format("%s/%s.json", path, key));
            JiraIssue jiraIssue = SerializationUtils.read(content, JiraIssue.class);
            return JiraIssueMapper.map(jiraIssue);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected List<Issue> getIssuesInEpic(String key) {
        try {
            String content = fromFile(format("%s/issues-in-epic-%s.json", path, key));
            SearchResponse searchResponse = SerializationUtils.read(content, SearchResponse.class);
            if (searchResponse.getTotal() == 0) return null;
            return searchResponse.getIssues().stream()
                    .sorted(Comparator.comparingInt(JiraIssue::getId))
                    .map(JiraIssueMapper::map)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

}
