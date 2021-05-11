package pw.avvero.jiac

import pw.avvero.jiac.core.JiacService
import pw.avvero.jiac.schema.Issue
import pw.avvero.jiac.schema.SchemaParser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static pw.avvero.jiac.core.Difference.*

class JiacServiceDiffCompositeTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There are no differences if there are no difference"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider)
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
    def "There are differences if summary is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff.size() == 5
        diff[0].issueKey == "WATCH-1"
        diff[0].type == SUMMARY_CHANGED
        diff[0].oldValue == "Working with jira issues as a code"
        diff[0].newValue == "Working with jira issues as a code (updated)"
        diff[1].issueKey == "WATCH-2"
        diff[1].type == SUMMARY_CHANGED
        diff[1].oldValue == "Prepare to do one thing"
        diff[1].newValue == "Prepare to do one thing (updated)"
        diff[0].issueKey == "WATCH-4"
        diff[3].type == SUMMARY_CHANGED
        diff[3].oldValue == "Prepare to do one thing part 2"
        diff[3].newValue == "Prepare to do one thing part 2 (updated)"
        diff[0].issueKey == "WATCH-5"
        diff[4].type == SUMMARY_CHANGED
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

    @Unroll
    def "There are no differences if type is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider)
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
        newOne = """# [WATCH-1:Story] Working with jira issues as a code
- [WATCH-2:Tak] Prepare to do one thing
- - [WATCH-3:Sub-bug] Prepare to do one thing part 1
- - [WATCH-4:Sub-bug] Prepare to do one thing part 2
- [WATCH-5:Task] Actually do one thing"""
    }

    @Unroll
    def "There are no differences if root key is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider)
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
        newOne = """# [WATCH-6:Story] Working with jira issues as a code"""
    }

    @Unroll
    def "There are no differences if the only change is order of tasks"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider)
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

    @Unroll
    def "There are differences if there are new issues"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff == []
        diff[0].type == NEW_ISSUE
        diff[0].newValue == new Issue(key: "Story", summary: "Prepare to do one thing")
        diff[1].type == NEW_ISSUE
        diff[1].newValue == new Issue(key: "Sub-task", summary: "Prepare to do one thing part 1")
        diff[2].type == NEW_ISSUE
        diff[2].newValue == new Issue(key: "Sub-task", summary: "Prepare to do one thing part 2")
        diff[3].type == NEW_ISSUE
        diff[3].newValue == new Issue(key: "Story", summary: "Actually do one thing")
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code"""
        newOne = """# [WATCH-1:Epic] Working with jira issues as a code
- [WATCH:Story] Prepare to do one thing
- - [WATCH:Sub-task] Prepare to do one thing part 1
- - [WATCH:Sub-task] Prepare to do one thing part 2
- [WATCH:Story] Actually do one thing"""
    }

    @Unroll
    def "There are differences if some issues are deleted"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider)
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
