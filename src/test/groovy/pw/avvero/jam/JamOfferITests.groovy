package pw.avvero.jam

import pw.avvero.jam.core.Issue
import pw.avvero.jam.jira.HttpApiClient
import pw.avvero.jam.jira.JiraApiDataProvider
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class JamOfferITests extends Specification {

    @Shared
    private String project = "WTA"
    @Shared
    private JamService jam

    def setupSpec() {
        JiraApiDataProvider jira = new JiraApiDataProvider(new HttpApiClient("http://localhost:8081", "admin", "admin"))
        jam = new JamService(jira)
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Do some stuff"))
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

    def "Schema offer and result returns the same schema with keys"() {
        expect:
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
        return value.replace("                    ", "")
    }
}
