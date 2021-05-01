package pw.avvero.jiac

import spock.lang.Specification
import spock.lang.Unroll

class SchemaParsingTests extends Specification {

    @Unroll
    def "Single line schema is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema)
        then:
        issue.project == "OKR"
        issue.type == "Task"
        issue.summary == "Working with jira issues as a code"
        where:
        schema                                                  | _
        "[OKR:Task]Working with jira issues as a code"          | _
        "# [OKR:Task]Working with jira issues as a code"        | _
        " [OKR:Task]Working with jira issues as a code   "      | _
        "# [OKR:Task] Working with jira issues as a code"       | _
        "[OKR:Task] Working with jira issues as a code"         | _
        "[  OKR  :  Task  ] Working with jira issues as a code" | _
        "  [OKR:Task]   Working with jira issues as a code"     | _
    }

}
