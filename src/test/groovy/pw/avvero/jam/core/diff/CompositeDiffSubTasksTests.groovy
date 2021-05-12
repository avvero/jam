package pw.avvero.jam.core.diff

import pw.avvero.jam.IssueMapDataProvider
import pw.avvero.jam.core.JamService
import pw.avvero.jam.schema.SchemaParser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static pw.avvero.jam.core.Difference.NEW_SUB_TASK

class CompositeDiffSubTasksTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There are differences if there are new sub tasks"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        dataProvider.put("WATCH-1", parser.parseFromString(oldOne))
        and:
        def diff = service.diff("WATCH-1", newOne)
        then:
        diff[0].type == NEW_SUB_TASK
        diff[0].parent.summary == "Prepare to do one thing"
        diff[0].child.type == "Sub-task"
        diff[0].child.summary == "Prepare to do one thing part 1"
        diff[1].type == NEW_SUB_TASK
        diff[1].parent.summary == "Prepare to do one thing"
        diff[1].child.type == "Sub-task"
        diff[1].child.summary == "Prepare to do one thing part 2"
        diff.size() == 2
        where:
        oldOne = """# [WATCH-1:Story] Prepare to do one thing"""
        newOne = """# [WATCH-1:Story] Prepare to do one thing
                    - [WATCH:Sub-task] Prepare to do one thing part 1
                    - [WATCH:Sub-task] Prepare to do one thing part 2"""
    }
}
