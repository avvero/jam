package pw.avvero.jiac.dsl

import pw.avvero.jiac.dsl.Issue
import pw.avvero.jiac.dsl.SchemaParser
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

    @Unroll
    def "Single line schema with leading and trailing lines is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema.toString())
        then:
        issue.project == "OKR"
        issue.type == "Task"
        issue.summary == "Working with jira issues as a code"
        where:
        schema = """
[OKR:Task]Working with jira issues as a code
"""
    }

    @Unroll
    def "Composite schema is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema)
        then:
        issue.project == "OKR"
        issue.type == "Story"
        issue.summary == "Working with jira issues as a code"
        issue.children == [
                new Issue(project: "WATCH", type: "Task", summary: "Prepare to do one thing"),
                new Issue(project: "WATCH", type: "Task", summary: "Actually do one thing"),
        ]
        where:
        schema = """[OKR:Story]Working with jira issues as a code
-[WATCH:Task]Prepare to do one thing
-[WATCH:Task]Actually do one thing"""
    }

    @Unroll
    def "Two level schema with leading and trailing lines is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema)
        then:
        issue.project == "OKR"
        issue.type == "Story"
        issue.summary == "Working with jira issues as a code"
        issue.children[0].project == "WATCH"
        issue.children[0].type == "Task"
        issue.children[0].summary == "Prepare to do one thing"
        issue.children[1].project == "WATCH"
        issue.children[1].type == "Task"
        issue.children[1].summary == "Actually do one thing"
        where:
        schema = """# [OKR:Story] Working with jira issues as a code
                    - [WATCH:Task] Prepare to do one thing
                    - [WATCH:Task] Actually do one thing
        """
    }

    @Unroll
    def "Three level schema with leading and trailing lines is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema)
        then:
        issue.project == "OKR"
        issue.type == "Story"
        issue.summary == "Working with jira issues as a code"
        issue.children[0].project == "WATCH"
        issue.children[0].type == "Task"
        issue.children[0].summary == "Prepare to do one thing"
        issue.children[0].children[0].project == "WATCH"
        issue.children[0].children[0].type == "Task"
        issue.children[0].children[0].summary == "Prepare to do one thing part 1"
        issue.children[0].children[1].project == "WATCH"
        issue.children[0].children[1].type == "Task"
        issue.children[0].children[1].summary == "Prepare to do one thing part 2"
        issue.children[1].project == "WATCH"
        issue.children[1].type == "Task"
        issue.children[1].summary == "Actually do one thing"
        where:
        schema = """# [OKR:Story] Working with jira issues as a code
                    - [WATCH:Task] Prepare to do one thing
                    - - [WATCH:Task] Prepare to do one thing part 1
                    - - [WATCH:Task] Prepare to do one thing part 2
                    - [WATCH:Task] Actually do one thing
        """
    }

}