package pw.avvero.jiac

import spock.lang.Specification

class IssueDataProviderTests extends Specification {

    def "Data provider provides issue with children using file api"() {
        when:
        def provider = new FileIssueDataProvider()
        def issue = provider.getWithChildren("WATCH-1")
        then:
        issue.project == "WATCH"
        issue.key == "WATCH-1"
        issue.type == "Epic"
        issue.summary == "Working with jira issues as a code"
        issue.children[0].project == "WATCH"
        issue.children[0].key == "WATCH-2"
        issue.children[0].type == "Story"
        issue.children[0].summary == "Prepare to do one thing"
        issue.children[0].children[0].project == "WATCH"
        issue.children[0].children[0].key == "WATCH-3"
        issue.children[0].children[0].type == "Sub-task"
        issue.children[0].children[0].summary == "Prepare to do one thing part 1"
        issue.children[0].children[1].project == "WATCH"
        issue.children[0].children[1].key == "WATCH-4"
        issue.children[0].children[1].type == "Sub-task"
        issue.children[0].children[1].summary == "Prepare to do one thing part 2"
        issue.children[1].project == "WATCH"
        issue.children[1].key == "WATCH-5"
        issue.children[1].type == "Story"
        issue.children[1].summary == "Actually do one thing"
    }

}
