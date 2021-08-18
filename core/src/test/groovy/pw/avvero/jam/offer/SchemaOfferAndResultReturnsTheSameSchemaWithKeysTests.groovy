package pw.avvero.jam.offer


import pw.avvero.jam.JamService
import pw.avvero.jam.core.Issue
import pw.avvero.jam.jira.HttpApiClient
import pw.avvero.jam.jira.JiraApiDataProvider
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class SchemaOfferAndResultReturnsTheSameSchemaWithKeysTests extends Specification {

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

    def "Schema offer and result returns the same schema with keys"() {
        when:
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Do some stuff"))
        then:
        jam.checkout("$project-1") == "# [$project-1:Story] Do some stuff"
        and:
        jam.offer(source) == trim(target)
        where:
        source = """# [$project-1:Story] Do some stuff
                    - [$project:Sub-task] Prepare to do one thing part 1
                    - [$project:Sub-task] Prepare to do one thing part 2"""
        target = """# [$project-1:Story] Do some stuff
                    - [$project-2:Sub-task] Prepare to do one thing part 1
                    - [$project-3:Sub-task] Prepare to do one thing part 2"""
    }

    private trim(value) {
        return value.replaceAll("\\n\\s+", "\\\n")
    }
}
