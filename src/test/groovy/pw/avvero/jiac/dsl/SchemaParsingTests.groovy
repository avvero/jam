package pw.avvero.jiac.dsl

import pw.avvero.jiac.dsl.Issue
import pw.avvero.jiac.dsl.SchemaParser
import spock.lang.Specification
import spock.lang.Unroll

import static pw.avvero.test.ResourceDataProvider.fromFile
import static pw.avvero.test.SerializationUtils.json

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

    @Unroll
    def "Four level schema with leading and trailing lines is parsed from string #schema"() {
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
        issue.children[0].children[0].type == "SubTask"
        issue.children[0].children[0].summary == "Prepare to do one thing part 1"
        issue.children[0].children[1].project == "WATCH"
        issue.children[0].children[1].type == "SubTask"
        issue.children[0].children[1].summary == "Prepare to do one thing part 2"
        issue.children[0].children[1].children[0].project == "WATCH"
        issue.children[0].children[1].children[0].type == "SubBug"
        issue.children[0].children[1].children[0].summary == "Part 2 is not working"
        issue.children[0].children[2].project == "WATCH"
        issue.children[0].children[2].type == "Bug"
        issue.children[0].children[2].summary == "Some error for whole task"
        issue.children[1].project == "WATCH"
        issue.children[1].type == "Task"
        issue.children[1].summary == "Actually do one thing"
        where:
        schema = """# [OKR:Story] Working with jira issues as a code
                    - [WATCH:Task] Prepare to do one thing
                    - - [WATCH:SubTask] Prepare to do one thing part 1
                    - - [WATCH:SubTask] Prepare to do one thing part 2
                    - - - [WATCH:SubBug] Part 2 is not working
                    - - [WATCH:Bug] Some error for whole task
                    - [WATCH:Task] Actually do one thing
        """
    }

    @Unroll
    def "Multi level schema is parsed from file #schema"() {
        when:
        def fromSchema = new SchemaParser().parseFromFile("src/test/resources/" + jiacFile)
        def fromJson = json(fromFile(jsonFile), Issue)
        then:
        fromSchema == fromJson
        where:
        jiacFile            | jsonFile
        "new-multilevel.md" | "new-multilevel.json"
    }
}
