package pw.avvero.jiac.jira


import pw.avvero.test.SerializationUtils
import spock.lang.Specification

import static pw.avvero.test.ResourceDataProvider.fromFile

class JiraIssueMapperTests extends Specification {

    def "Issue could be mapped from jira epic"() {
        when:
        def issue = JiraIssueMapper.map(SerializationUtils.read(fromFile("jira/" + file), JiraIssue))
        then:
        issue.project == "WATCH"
        issue.key == "WATCH-1"
        issue.type == "Epic"
        issue.summary == "Working with jira issues as a code"
        where:
        file = "epic-no-children.json"
    }

    def "Issue could be mapped from story epic"() {
        when:
        def issue = JiraIssueMapper.map(SerializationUtils.read(fromFile("jira/" + file), JiraIssue))
        then:
        issue.project == "WATCH"
        issue.key == "WATCH-2"
        issue.type == "Story"
        issue.summary == "Prepare to do one thing"
        where:
        file = "story-no-children.json"
    }

}
