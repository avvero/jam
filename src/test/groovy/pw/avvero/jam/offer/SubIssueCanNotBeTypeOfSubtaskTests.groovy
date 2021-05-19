package pw.avvero.jam.offer

import pw.avvero.jam.JamException
import pw.avvero.jam.JamService
import pw.avvero.jam.core.Issue
import pw.avvero.jam.jira.HttpApiClient
import pw.avvero.jam.jira.JiraApiDataProvider
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

@Stepwise
class SubIssueCanNotBeTypeOfSubtaskTests extends Specification {

    @Shared
    private String project = "WTJ"
    @Shared
    private JamService jam
    @Shared
    private JiraApiDataProvider jira

    def setupSpec() {
        jira = new JiraApiDataProvider(new HttpApiClient("http://localhost:8081", "admin", "admin"))
        jam = new JamService(jira)
        jira.createProject(project, "Test-$project")
    }

    def cleanupSpec() {
        jira.deleteProject(project)
    }

    @Unroll
    def "Sub issue can't be type #subTaskType"() {
        when:
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Do some stuff"))
        jam.offer("""# [$project-1:Story] Do some stuff
                     - [$project:$subTaskType] Prepare to do one thing part 1""")
        then:
        def e = thrown(JamException)
        e.message == "Issue type $issutType is not a sub-task but a parent is specified."
        where:
        subTaskType | issutType
        "Story"     | 10001
        "Epic"      | 10000
    }
}
