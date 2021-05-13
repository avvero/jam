package pw.avvero.jam.integrational

import pw.avvero.jam.core.JamService
import pw.avvero.jam.jira.JiraApiDataProvider
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class Scenarios extends Specification {

    def "Create stories with tasks"() {
        setup:
        def apiAdapter = new JiraApiDataProvider("http://localhost:8081", "admin", "admin")
        def jamService = new JamService(apiAdapter)
        when:
        jamService.offer(schema)
        then:
        1 == 1
        where:
        schema = """# [WATCH-2:Story] Do some stuff
                    - [WATCH:Sub-task] Prepare to do one thing part 1
                    - [WATCH:Sub-task] Prepare to do one thing part 2"""
    }

}
