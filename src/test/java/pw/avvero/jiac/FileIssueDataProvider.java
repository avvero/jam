package pw.avvero.jiac;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pw.avvero.jiac.entity.Issue;
import pw.avvero.jiac.jira.JiraIssue;
import pw.avvero.jiac.jira.JiraIssueMapper;
import pw.avvero.jiac.jira.SearchResponse;
import pw.avvero.test.ResourceDataProvider;
import pw.avvero.test.SerializationUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static pw.avvero.test.ResourceDataProvider.fromFile;

@AllArgsConstructor
public class FileIssueDataProvider extends IssueDataProvider {

    private String path;

    @Override
    public Issue getByCode(String key) {
        try {
            String content = fromFile(format("%s/%s.json", path, key));
            JiraIssue jiraIssue = SerializationUtils.read(content, JiraIssue.class);
            return JiraIssueMapper.map(jiraIssue);
        } catch (IOException e) {
            System.out.println(e);
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
