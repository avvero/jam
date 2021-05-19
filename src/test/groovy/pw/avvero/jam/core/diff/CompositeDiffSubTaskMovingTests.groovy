package pw.avvero.jam.core.diff

import pw.avvero.jam.IssueMapDataProvider
import pw.avvero.jam.JamService
import pw.avvero.jam.core.DifferenceNewSubTask
import pw.avvero.jam.core.DifferenceSubTaskChangesParent
import pw.avvero.jam.schema.SchemaParser
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CompositeDiffSubTaskMovingTests extends Specification {

    @Shared
    def parser = new SchemaParser()

    @Unroll
    def "There are differences if subtask is moved to other story"() {
        setup:
        def dataProvider = new IssueMapDataProvider()
        def service = new JamService(dataProvider)
        when:
        def story1 = """# [WATCH-1:Story] Do one thing
                        - [WATCH-3:Sub-task] Prepare to do one thing part 1"""
        dataProvider.put("WATCH-1", parser.parseFromString(story1))
        def story2 = """# [WATCH-2:Story] Do second thing"""
        dataProvider.put("WATCH-2", parser.parseFromString(story2))
        and:
        def offer = """# [WATCH-2:Story] Do second thing
                       - [WATCH-3:Sub-task] Prepare to do one thing part 1"""
        def diff = service.diff("WATCH-2", offer)
        then:
        diff[0] instanceof DifferenceSubTaskChangesParent
        diff[0].parent.summary == "Do second thing"
        diff[0].child.type == "Sub-task"
        diff[0].child.summary == "Prepare to do one thing part 1"
        diff.size() == 1
    }
}
