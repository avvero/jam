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
class JamOfferIntegrationTests extends Specification {

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

    @Unroll
    def "Sub issue can be type #subTaskType"() {
        when:
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Do some stuff"))

        def source = """# [$project-2:Story] Do some stuff
                        - [$project:Sub-task] Prepare to do one thing part 1"""
        def target = """# [$project-2:Story] Do some stuff
                        - [$project-4:Sub-task] Prepare to do one thing part 1"""
        then:
        jam.offer(source) == trim(target)
        where:
        subTaskType | _
        "Sub-task"  | _
    }

    def "Schema offer and result returns the same schema with keys"() {
        when:
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Do some stuff"))
        then:
        jam.checkout("$project-3") == "# [$project-3:Story] Do some stuff"
        and:
        jam.offer(source) == trim(target)
        where:
        source = """# [$project-3:Story] Do some stuff
                    - [$project:Sub-task] Prepare to do one thing part 1
                    - [$project:Sub-task] Prepare to do one thing part 2"""
        target = """# [$project-3:Story] Do some stuff
                    - [$project-6:Sub-task] Prepare to do one thing part 1
                    - [$project-7:Sub-task] Prepare to do one thing part 2"""
    }

    def "Schema offer contains a moving existed sub task to new story"() {
        when:
        jira.createIssue(new Issue(project: project, type: "Story", summary: "First story with subtask"))
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Second story with subtask"))
        then:
        jam.checkout("$project-8") == "# [$project-8:Story] First story with subtask"
        jam.checkout("$project-9") == "# [$project-9:Story] Second story with subtask"
        when:
        def source = """# [$project-8:Story] First story with subtask
                        - [$project:Sub-task] Prepare to do one thing part"""
        def target = """# [$project-8:Story] First story with subtask
                        - [$project-10:Sub-task] Prepare to do one thing part"""
        then:
        jam.offer(source) == trim(target)
        when:
        source = """# [$project-9:Story] Second story with subtask
                    - [$project-10:Sub-task] Prepare to do one thing part"""
        target = """# [$project-9:Story] Second story with subtask
                    - [$project-10:Sub-task] Prepare to do one thing part"""
        and: "Subtask can't be moved"
        jam.offer(source)
        then:
        def e = thrown(JamException)
        e.message == "Move to the new parent is not supported for subtasks now"
//        then: "Subtask is moved to new story"
//        jam.offer(source) == trim(target)
//        jam.checkout("$project-8") == """# [$project-8:Story] Second story with subtask"""
//        jam.checkout("$project-9") == """# [$project-9:Story] Second story with subtask
//                                         - [$project-10:Sub-task] Prepare to do one thing part"""
    }

    private trim(value) {
        return value.replaceAll("\\n\\s+", "\\\n")
    }
}
