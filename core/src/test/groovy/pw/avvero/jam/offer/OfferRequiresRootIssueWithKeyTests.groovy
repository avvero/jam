package pw.avvero.jam.offer

import pw.avvero.jam.JamException
import pw.avvero.jam.JamService
import pw.avvero.jam.jira.HttpApiClient
import pw.avvero.jam.jira.JiraApiDataProvider
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class OfferRequiresRootIssueWithKeyTests extends Specification {

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

    def "Offer requires root issue with key"() {
        when:
        jam.offer(source)
        then:
        def e = thrown(JamException)
        e.message == "Please provide key for root issue"
        where:
        source = """# [$project:Story] Do some stuff
                    - [$project:Sub-task] Prepare to do one thing part 1
                    - [$project:Sub-task] Prepare to do one thing part 2"""
    }
}
