package pw.avvero.jiac;

import lombok.AllArgsConstructor;
import pw.avvero.jiac.entity.Issue;
import pw.avvero.jiac.jira.JiraIssue;
import pw.avvero.jiac.jira.JiraIssueMapper;
import pw.avvero.jiac.jira.SearchResponse;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class IssueApiDataProvider extends IssueDataProvider {

    private final String host;
    private final String username;
    private final String password;

    @Override
    public Issue getByCode(String key) {
        JiraIssue issue = requestGet(host + "/rest/api/latest/issue/" + key, JiraIssue.class);
        return JiraIssueMapper.map(issue);
    }

    @Override
    protected List<Issue> getIssuesInEpic(String key) {
        // TODO improve concatenation
        String url = host + "/rest/api/latest/search?jql=%22Epic%20Link%22=" + key + "&expand=schema,names,children";
        SearchResponse response = requestGet(url, SearchResponse.class);
        if (response.getTotal() == 0) return null;
        return response.getIssues().stream()
                .sorted(Comparator.comparingInt(JiraIssue::getId))
                .map(JiraIssueMapper::map)
                .collect(Collectors.toList());
    }

    private <T> T requestGet(String url, Class<T> clazz) {
        try {
            System.out.println("Calling: " + url);
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(url))
                    .headers("Authorization", authHeader)
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return SerializationUtils.read(response.body(), clazz);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }
}