package pw.avvero.jam.jira

import pw.avvero.jam.jira.JiraIssueMapper
import pw.avvero.jam.jira.dto.JiraIssue
import spock.lang.Specification

import static pw.avvero.jam.core.SerializationUtils.read
import static pw.avvero.test.ResourceDataProvider.fromFile

class JiraIssueMapperTests extends Specification {

    def "Issue could be mapped from jira epic"() {
        when:
        def issue = JiraIssueMapper.map(read(fromFile("jira/epic-no-children.json"), JiraIssue), null)
        then:
        issue.project == "WATCH"
        issue.key == "WATCH-1"
        issue.type == "Epic"
        issue.parent == null
        issue.summary == "Working with jira issues as a code"
    }

    def "Issue could be mapped from jira story"() {
        when:
        def issue = JiraIssueMapper.map(read(fromFile("jira/story-no-children.json"), JiraIssue), null)
        then:
        issue.project == "WATCH"
        issue.key == "WATCH-2"
        issue.type == "Story"
        issue.parent == null
        issue.summary == "Prepare to do one thing"
    }

    def "Issue with children and links could be mapped from jira story with subtasks"() {
        when:
        def issue = JiraIssueMapper.map(read(fromFile("jira/story-with-subtasks.json"), JiraIssue), null)
        then:
        issue.project == "WATCH"
        issue.key == "WATCH-2"
        issue.type == "Story"
        issue.parent == null
        issue.summary == "Prepare to do one thing"
        issue.children[0].project == "WATCH"
        issue.children[0].key == "WATCH-3"
        issue.children[0].type == "Sub-task"
        issue.children[0].parent.key == "WATCH-2"
        issue.children[0].summary == "Prepare to do one thing part 1"
        issue.children[1].project == "WATCH"
        issue.children[1].key == "WATCH-4"
        issue.children[1].type == "Sub-task"
        issue.children[0].parent.key == "WATCH-2"
        issue.children[1].summary == "Prepare to do one thing part 2"
        issue.links[0].type == "blocks"
        issue.links[0].issue.key == "WATCH-5"
        issue.links[1].type == "is blocked by"
        issue.links[1].issue.key == "WATCH-6"
    }

}
