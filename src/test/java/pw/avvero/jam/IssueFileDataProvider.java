package pw.avvero.jam;

import lombok.AllArgsConstructor;
import pw.avvero.jam.core.IssueDataProvider;
import pw.avvero.jam.core.SerializationUtils;
import pw.avvero.jam.jira.dto.JiraIssue;
import pw.avvero.jam.jira.dto.SearchResponse;
import pw.avvero.jam.core.Issue;
import pw.avvero.jam.jira.JiraIssueMapper;

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
            return JiraIssueMapper.map(jiraIssue, null);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected List<Issue> getIssuesInEpic(String key, Issue epic) {
        try {
            String content = fromFile(format("%s/issues-in-epic-%s.json", path, key));
            SearchResponse searchResponse = SerializationUtils.read(content, SearchResponse.class);
            if (searchResponse.getTotal() == 0) return null;
            return searchResponse.getIssues().stream()
                    .sorted(Comparator.comparingInt(JiraIssue::getId))
                    .map(c -> JiraIssueMapper.map(c, epic))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void updateSummary(String key, String newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String addSubTask(Issue parent, Issue child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addIssueToEpic(Issue epic, Issue issue) {
        throw new UnsupportedOperationException();
    }

}
