package pw.avvero.jam.core.diff

import pw.avvero.jam.IssueMapDataProvider
import pw.avvero.jam.core.DifferenceSummary
import pw.avvero.jam.JamService
import pw.avvero.jam.schema.SchemaParser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CompositeDiffSummaryTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There are differences if summary is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff.size() == 5
        diff[0].issueKey == "WATCH-1"
        diff[0] instanceof DifferenceSummary
        diff[0].oldValue == "Working with jira issues as a code"
        diff[0].newValue == "Working with jira issues as a code (updated)"
        diff[1].issueKey == "WATCH-2"
        diff[1] instanceof DifferenceSummary
        diff[1].oldValue == "Prepare to do one thing"
        diff[1].newValue == "Prepare to do one thing (updated)"
        diff[3].issueKey == "WATCH-4"
        diff[3] instanceof DifferenceSummary
        diff[3].oldValue == "Prepare to do one thing part 2"
        diff[3].newValue == "Prepare to do one thing part 2 (updated)"
        diff[4].issueKey == "WATCH-5"
        diff[4] instanceof DifferenceSummary
        diff[4].oldValue == "Actually do one thing"
        diff[4].newValue == "Actually do one thing (updated)"
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2
                    - [WATCH-5:Story] Actually do one thing"""
        newOne = """# [WATCH-1:Epic] Working with jira issues as a code (updated)
                    - [WATCH-2:Story] Prepare to do one thing (updated)
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1 (updated)
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2 (updated)
                    - [WATCH-5:Story] Actually do one thing (updated)"""
    }
}
