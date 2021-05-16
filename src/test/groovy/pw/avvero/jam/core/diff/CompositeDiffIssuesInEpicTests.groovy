package pw.avvero.jam.core.diff

import pw.avvero.jam.IssueMapDataProvider
import pw.avvero.jam.core.DifferenceNewIssueInEpic
import pw.avvero.jam.JamService
import pw.avvero.jam.schema.SchemaParser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CompositeDiffIssuesInEpicTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There are differences if there are new issues in epic"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff[0] instanceof DifferenceNewIssueInEpic
        diff[0].epic.key == "WATCH-1"
        diff[0].issue.type == "Story"
        diff[0].issue.summary == "Prepare to do one thing"
        diff[1] instanceof DifferenceNewIssueInEpic
        diff[1].epic.key == "WATCH-1"
        diff[1].issue.type == "Story"
        diff[1].issue.summary == "Actually do one thing"
        diff.size() == 2
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code"""
        newOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH:Story] Prepare to do one thing
                    - [WATCH:Story] Actually do one thing"""
    }
}
