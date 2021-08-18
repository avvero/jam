package pw.avvero.jam.offer

import pw.avvero.jam.JamException
import pw.avvero.jam.JamService
import pw.avvero.jam.core.Issue
import pw.avvero.jam.jira.HttpApiClient
import pw.avvero.jam.jira.JiraApiDataProvider
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class MovingExistedSubTaskToNewStoryTests extends Specification {

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


    def "Moving existed subtask to new story is not supported"() {
        when:
        jira.createIssue(new Issue(project: project, type: "Story", summary: "First story with subtask"))
        jira.createIssue(new Issue(project: project, type: "Story", summary: "Second story with subtask"))
        then:
        jam.checkout("$project-1") == "# [$project-1:Story] First story with subtask"
        jam.checkout("$project-2") == "# [$project-2:Story] Second story with subtask"
        when:
        def source = """# [$project-1:Story] First story with subtask
                        - [$project:Sub-task] Prepare to do one thing part"""
        def target = """# [$project-1:Story] First story with subtask
                        - [$project-3:Sub-task] Prepare to do one thing part"""
        then:
        jam.offer(source) == trim(target)
        when:
        source = """# [$project-2:Story] Second story with subtask
                    - [$project-3:Sub-task] Prepare to do one thing part"""
        target = """# [$project-2:Story] Second story with subtask
                    - [$project-3:Sub-task] Prepare to do one thing part"""
        and: "Subtask can't be moved"
        jam.offer(source)
        then:
        def e = thrown(JamException)
        e.message == "Move to the new parent is not supported for subtasks now"
//        then: "Subtask is moved to new story"
//        jam.offer(source) == trim(target)
//        jam.checkout("$project-1") == """# [$project-1:Story] Second story with subtask"""
//        jam.checkout("$project-2") == """# [$project-2:Story] Second story with subtask
//                                         - [$project-3:Sub-task] Prepare to do one thing part"""
    }

    private trim(value) {
        return value.replaceAll("\\n\\s+", "\\\n")
    }
}
