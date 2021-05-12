package pw.avvero.jam.core.diff

import pw.avvero.jam.IssueMapDataProvider
import pw.avvero.jam.core.IssueComparisonException
import pw.avvero.jam.core.JamService
import pw.avvero.jam.schema.SchemaParser
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static pw.avvero.jam.core.Difference.ISSUE_ABSENT

class CompositeDiffCommonTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There are no differences if there are no difference"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff == []
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2
                    - [WATCH-5:Story] Actually do one thing"""
        newOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2
                    - [WATCH-5:Story] Actually do one thing"""
    }

    @Unroll
    def "There are no differences if root key is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        service.diff("WATCH-1", newOne)
        then:
        thrown(IssueComparisonException)
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2
                    - [WATCH-5:Story] Actually do one thing"""
        newOne = """# [WATCH-6:Story] Working with jira issues as a code"""
    }

    @Unroll
    def "There are no differences if the only change is order of tasks"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff == []
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2
                    - [WATCH-5:Story] Actually do one thing"""
        newOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-5:Story] Actually do one thing
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1"""
    }

    @Ignore("Not ready to provide it now")
    @Unroll
    def "There are differences if some issues are deleted"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff.size() == 2
        diff[0].issueKey == "WATCH-3"
        diff[0].type == ISSUE_ABSENT
        diff[0].issueKey == "WATCH-5"
        diff[0].type == ISSUE_ABSENT
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-3:Sub-task] Prepare to do one thing part 1
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2
                    - [WATCH-5:Story] Actually do one thing"""
        newOne = """# [WATCH-1:Epic] Working with jira issues as a code
                    - [WATCH-2:Story] Prepare to do one thing
                    - - [WATCH-4:Sub-task] Prepare to do one thing part 2"""
    }
}
