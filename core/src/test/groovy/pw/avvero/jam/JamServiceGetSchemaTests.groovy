package pw.avvero.jam

import pw.avvero.jam.IssueFileDataProvider
import pw.avvero.jam.JamService
import spock.lang.Shared
import spock.lang.Specification

class JamServiceGetSchemaTests extends Specification {

    @Shared
    def service = new JamService(new IssueFileDataProvider("jira/file-api"))

    def "Method getSchemaForIssueWithChildren returns schema with children for epic with children"() {
        when:
        def schema = service.getSchemaForIssueWithChildren("WATCH-1")
        then:
        schema == """# [WATCH-1:Epic] Working with jira issues as a code
- [WATCH-2:Story] Prepare to do one thing
- - [WATCH-3:Sub-task] Prepare to do one thing part 1
- - [WATCH-4:Sub-task] Prepare to do one thing part 2
- [WATCH-5:Story] Actually do one thing"""
    }

    def "Method getSchemaForIssueWithChildren returns issue without children for empty epic"() {
        when:
        def schema = service.getSchemaForIssueWithChildren("WATCH-6")
        then:
        schema == """# [WATCH-6:Epic] Empty epic"""
    }

}
