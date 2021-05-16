package pw.avvero.jam.jira;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pw.avvero.jam.core.IssueDataProvider;
import pw.avvero.jam.jira.dto.*;
import pw.avvero.jam.core.Issue;

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

import static pw.avvero.jam.core.SerializationUtils.read;
import static pw.avvero.jam.core.SerializationUtils.stringify;

@Slf4j
@AllArgsConstructor
public class JiraApiDataProvider extends IssueDataProvider {

    private final String host;
    private final String username;
    private final String password;

    @Override
    public Issue getByCode(String key) {
        JiraIssue issue = requestGet(host + "/rest/api/latest/issue/" + key, JiraIssue.class);
        return JiraIssueMapper.map(issue, null);
    }

    @Override
    protected List<Issue> getIssuesInEpic(String key, Issue epic) {
        // TODO improve concatenation
        String url = host + "/rest/api/latest/search?jql=%22Epic%20Link%22=" + key + "&expand=schema,names,children";
        SearchResponse response = requestGet(url, SearchResponse.class);
        if (response.getTotal() == 0) return null;
        return response.getIssues().stream()
                .sorted(Comparator.comparingInt(JiraIssue::getId))
                .map(i -> JiraIssueMapper.map(i, epic))
                .collect(Collectors.toList());
    }

    @Override
    public void updateSummary(String key, String newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String addSubTask(Issue parent, Issue child) {
        log.info("Adding task to issue: " + parent.getKey());
        IssueType issueType = getIssueType(child.getProject(), child.getType());
        JiraIssue jiraIssue = JiraIssue.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key(child.getProject()).build())
                        .parent(JiraIssue.builder().key(parent.getKey()).build())
                        .summary(child.getSummary())
                        .issuetype(issueType)
                        .build())
                .build();
        JiraIssue response = requestPost(host + "/rest/api/2/issue", jiraIssue, JiraIssue.class);
        return response.getKey();
    }

    @Override
    public void addIssueToEpic(Issue epic, Issue issue) {
        throw new UnsupportedOperationException();
    }

    private IssueType getIssueType(String projectKey, String type) {
        CreatemetaResponse response = createmeta(projectKey);
        for (Project project : response.getProjects()) {
            for (IssueType issueType : project.getIssuetypes()) {
                if (issueType.getName().equalsIgnoreCase(type)) {
                    return issueType;
                }
            }
        }
        return null;
    }

    private CreatemetaResponse createmeta(String projectKey) {
        String url = host + "/rest/api/latest/issue/createmeta?projectKeys=" + projectKey;
        return requestGet(url, CreatemetaResponse.class);
    }

    private <T> T requestGet(String url, Class<T> clazz) {
        try {
            log.debug("Calling: " + url);
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
            log.debug("Response payload: " + response.body());
            return read(response.body(), clazz);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    private <T> T requestPost(String url, Object payload, Class<T> clazz) {
        try {
            log.debug("Calling: " + url);
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);

            String payloadString = stringify(payload);
            log.debug("Payload: " + payloadString);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .headers("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payloadString))
                    .build();
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            log.debug("Response code: " + response.statusCode());
            String responseBody = response.body();
            log.debug("Response payload: " + responseBody);
            return read(responseBody, clazz);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }
}