package pw.avvero.jam.offer


import pw.avvero.jam.JamService
import pw.avvero.jam.core.Issue
import pw.avvero.jam.jira.HttpApiClient
import pw.avvero.jam.jira.JiraApiDataProvider
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

@Stepwise
class SubIssueCanBeTypeOfSubtaskTests extends Specification {

    @Shared
    private String project = "WTJ"
    @Shared
    private JamService jam
    @Shared
    private JiraApiDataProvider jira

    def setupSpec() {
        jira = new JiraApiDataProvider(new HttpApiClient("http://localhost:8081", "admin", "admin", 30000))
        jam = new JamService(jira)
        jira.createProject(project, "Test-$project")
    }

    def cleanupSpec() {
        jira.deleteProject(project)
    }

    @Unroll
    def "Sub issue can be type #subTaskType"() {
        when:
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Do some stuff"))

        def source = """# [$project-1:Story] Do some stuff
                        - [$project:Sub-task] Prepare to do one thing part 1"""
        def target = """# [$project-1:Story] Do some stuff
                        - [$project-2:Sub-task] Prepare to do one thing part 1"""
        then:
        jam.offer(source) == trim(target)
        where:
        subTaskType | _
        "Sub-task"  | _
    }

    private trim(value) {
        return value.replaceAll("\\n\\s+", "\\\n")
    }

}
