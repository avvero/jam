package pw.avvero.jam.core.diff

import pw.avvero.jam.IssueMapDataProvider
import pw.avvero.jam.JamService
import pw.avvero.jam.schema.SchemaParser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CompositeDiffTypeTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There are no differences if type is different"() {
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
        newOne = """# [WATCH-1:Story] Working with jira issues as a code
                    - [WATCH-2:Tak] Prepare to do one thing
                    - - [WATCH-3:Sub-bug] Prepare to do one thing part 1
                    - - [WATCH-4:Sub-bug] Prepare to do one thing part 2
                    - [WATCH-5:Task] Actually do one thing"""
    }
}
