package pw.avvero.jiac

import pw.avvero.jiac.schema.SchemaParser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static pw.avvero.jiac.Difference.SUMMARY_CHANGED

class JiacServiceDiffSingleTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There is no difference if there is no difference"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider, new SchemaParser())
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff == []
        where:
        newOne                                                        | _
        """# [WATCH-1:Epic] Working with jira issues as a code"""     | _
        """# [WATCH-1 : Epic ] Working with jira issues as a code """ | _
        """#[WATCH-1:Epic] Working with jira issues as a code"""      | _
        """# [WATCH-1:Epic]  Working with jira issues as a code"""    | _
        """# [WATCH-1:Epic] Working with jira issues as a code """    | _

        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code"""
    }

    def "There is a difference if summary is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider, new SchemaParser())
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff.size() == 1
        diff[0].issue.key == "WATCH-1"
        diff[0].type == SUMMARY_CHANGED
        diff[0].oldValue == "Working with jira issues as a code"
        diff[0].newValue == "Working with jira issues as a code (updated)"
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code"""
        newOne = """# [WATCH-1:Epic] Working with jira issues as a code (updated)"""
    }

    def "There is no difference if type is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider, new SchemaParser())
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff == []
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code"""
        newOne = """# [WATCH-1:Story] Working with jira issues as a code"""
    }

    def "There is no difference if root key is different"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JiacService(dataProvider, new SchemaParser())
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff == []
        where:
        oldOne = """# [WATCH-1:Epic] Working with jira issues as a code"""
        newOne = """# [WATCH-2:Epic] Working with jira issues as a code"""
    }

}
