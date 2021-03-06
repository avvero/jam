package pw.avvero.jam.jira;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pw.avvero.jam.JamException;
import pw.avvero.jam.core.Issue;
import pw.avvero.jam.core.IssueDataProvider;
import pw.avvero.jam.jira.dto.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class JiraApiDataProvider extends IssueDataProvider {

    private final HttpApiClient httpApiClient;

    @Override
    public Issue getByCode(String key) {
        JiraIssue issue = httpApiClient.requestGet("/rest/api/latest/issue/" + key, JiraIssue.class);
        if (issue == null) return null;
        return JiraIssueMapper.map(issue, null);
    }

    @Override
    protected List<Issue> getIssuesInEpic(String key, Issue epic) {
        // TODO improve concatenation
        String method = "/rest/api/latest/search?jql=%22Epic%20Link%22=" + key + "&expand=schema,names,children";
        SearchResponse response = httpApiClient.requestGet(method, SearchResponse.class);
        if (response.getTotal() == 0) return null;
        return response.getIssues().stream()
                .sorted(Comparator.comparingInt(JiraIssue::getId))
                .map(i -> JiraIssueMapper.map(i, epic))
                .collect(Collectors.toList());
    }

    @Override
    public void updateSummary(String key, String newValue) {
        log.info("Updating issue: " + key);
        JiraIssue jiraIssue = JiraIssue.builder()
                .fields(Fields.builder()
                        .summary(newValue)
                        .build())
                .build();
        httpApiClient.requestPut("/rest/api/latest/issue/" + key, jiraIssue, JiraIssue.class);
    }

    public JiraIssue createIssue(Issue issue) throws JamException {
        log.info("Creating issue: " + issue.getKey());
        IssueType issueType = getIssueType(issue.getProject(), issue.getType());
        JiraIssue jiraIssue = JiraIssue.builder()
                .fields(Fields.builder()
                        .project(Project.builder().key(issue.getProject()).build())
                        .summary(issue.getSummary())
                        .issuetype(issueType)
                        .build())
                .build();
        JiraIssue response = httpApiClient.requestPost("/rest/api/latest/issue", jiraIssue, JiraIssue.class);
        jiraIssue.setId(response.getId());
        jiraIssue.setKey(response.getKey());
        return jiraIssue;
    }

    @Override
    public String addSubTask(Issue parent, Issue child) throws JamException {
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
        JiraIssue response = httpApiClient.requestPost("/rest/api/latest/issue", jiraIssue, JiraIssue.class);
        return response.getKey();
    }

    @Override
    public void addIssueToEpic(Issue epic, Issue issue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void moveSubTaskToParent(Issue parent, Issue child) throws JamException {
        throw new JamException("Move to the new parent is not supported for subtasks now");
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
        String method = "/rest/api/latest/issue/createmeta?projectKeys=" + projectKey;
        return httpApiClient.requestGet(method, CreatemetaResponse.class);
    }

    public Project createProject(String key, String name) throws JamException {
        log.info("Creating project: " + key);
        Map<String, String> request = new HashMap<>();
        request.put("assigneeType", "UNASSIGNED");
        request.put("projectTypeKey", "software");
        request.put("projectTemplateKey", "com.pyxis.greenhopper.jira:gh-scrum-template");
        request.put("key", key);
        request.put("name", name);
        request.put("lead", "admin");
        Project response = httpApiClient.requestPost("/rest/api/latest/project", request, Project.class);
        return response;
    }

    public void deleteProject(String key) {
        httpApiClient.requestDelete("/rest/api/latest/project/" + key);
    }

}