package pw.avvero.jiac.schema

import spock.lang.Specification
import spock.lang.Unroll

import static pw.avvero.jiac.core.SerializationUtils.read
import static pw.avvero.test.ResourceDataProvider.fromFile

class SchemaParserTests extends Specification {

    @Unroll
    def "Single line schema is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema)
        then:
        issue.key == key
        issue.project == "OKR"
        issue.type == type
        issue.summary == summary
        where:
        schema                                | key      | type       | summary
        "[OKR:Task]Do some stuff"             | null     | "Task"     | "Do some stuff"
        "# [OKR:Task]Do some stuff"           | null     | "Task"     | "Do some stuff"
        " [OKR:Task]Do some stuff   "         | null     | "Task"     | "Do some stuff"
        "# [OKR:Task] Do some stuff"          | null     | "Task"     | "Do some stuff"
        "[OKR:Task] Do some stuff"            | null     | "Task"     | "Do some stuff"
        "[  OKR  :  Task  ] Do some stuff"    | null     | "Task"     | "Do some stuff"
        "  [OKR:Task]   Do some stuff"        | null     | "Task"     | "Do some stuff"
        "[OKR-12:Task]Do some stuff"          | "OKR-12" | "Task"     | "Do some stuff"
        "# [OKR-12:Task]Do some stuff"        | "OKR-12" | "Task"     | "Do some stuff"
        " [OKR-12:Task]Do some stuff   "      | "OKR-12" | "Task"     | "Do some stuff"
        "# [OKR-12:Task] Do some stuff"       | "OKR-12" | "Task"     | "Do some stuff"
        "[OKR-12:Task] Do some stuff"         | "OKR-12" | "Task"     | "Do some stuff"
        "[  OKR-12  :  Task  ] Do some stuff" | "OKR-12" | "Task"     | "Do some stuff"
        "  [OKR-12:Task]   Do some stuff"     | "OKR-12" | "Task"     | "Do some stuff"
        "# [OKR-12:Sub Task] Do some stuff"   | "OKR-12" | "Sub Task" | "Do some stuff"
        "# [OKR-12:Sub-Task] Do some stuff"   | "OKR-12" | "Sub-Task" | "Do some stuff"
        "# [OKR-12:Task] Do some stuff: one"  | "OKR-12" | "Task"     | "Do some stuff: one"
        "# [OKR-12:Task] Do some stuff, one"  | "OKR-12" | "Task"     | "Do some stuff, one"
        "# [OKR-12:Task] Do some stuff. one"  | "OKR-12" | "Task"     | "Do some stuff. one"
        "# [OKR-12:Task] Do some stuff? one"  | "OKR-12" | "Task"     | "Do some stuff? one"
    }

    @Unroll
    def "Single line schema with leading and trailing lines is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema.toString())
        then:
        issue.project == "OKR"
        issue.type == "Task"
        issue.summary == "Do some stuff"
        where:
        schema = """
                [OKR:Task]Do some stuff
                """
    }

    @Unroll
    def "Composite schema is parsed from string #schema"() {
        when:
        def issue = new SchemaParser().parseFromString(schema)
        then:
        issue.project == "OKR"
        issue.type == "Story"
        issue.summary == "Do some stuff"
        issue.children == [
                new Issue(project: "WATCH", type: "Task", summary: "Prepare to do one thing"),
                new Issue(project: "WATCH", type: "Task", summary: "Actually do one thing"),
        ]
        where:
        schema = """[OKR:Story]Do some stuff
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
        issue.summary == "Do some stuff"
        issue.children[0].project == "WATCH"
        issue.children[0].type == "Task"
        issue.children[0].summary == "Prepare to do one thing"
        issue.children[1].project == "WATCH"
        issue.children[1].type == "Task"
        issue.children[1].summary == "Actually do one thing"
        where:
        schema = """# [OKR:Story] Do some stuff
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
        issue.summary == "Do some stuff"
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
        schema = """# [OKR:Story] Do some stuff
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
        issue.summary == "Do some stuff"
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
        schema = """# [OKR:Story] Do some stuff
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
        def fromSchema = new SchemaParser().parseFromFile("src/test/resources/jiac/" + jiacFile)
        def fromJson = read(fromFile("jiac/" + jsonFile), Issue)
        then:
        fromSchema == fromJson
        where:
        jiacFile                | jsonFile
        "new-multilevel.md"     | "new-multilevel.json"
        "existed-multilevel.md" | "existed-multilevel.json"
    }

    @Unroll
    def "SchemaParsingError is thrown if schema is incorrect"() {
        when:
        new SchemaParser().parseFromString(schema)
        then:
        thrown(SchemaParsingError)
        where:
        schema | _
        null   | _
        ""     | _
        "foo"  | _
    }
}
