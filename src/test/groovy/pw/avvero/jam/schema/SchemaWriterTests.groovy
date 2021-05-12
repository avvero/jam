package pw.avvero.jam.schema

import pw.avvero.jam.schema.Issue
import pw.avvero.jam.schema.SchemaWriter
import spock.lang.Specification
import spock.lang.Unroll

import static pw.avvero.jam.core.SerializationUtils.read
import static pw.avvero.test.ResourceDataProvider.fromFile

class SchemaWriterTests extends Specification {

    @Unroll
    def "Issue #issue would be written to #schema"() {
        expect:
        SchemaWriter.toString(issue) == schema
        where:
        issue                                                                               | schema
        new Issue(project: "WATCH", type: "Task", summary: "Do some stuff")                 | "# [WATCH:Task] Do some stuff"
        new Issue(project: "WATCH", key: "WATCH-1", type: "Task", summary: "Do some stuff") | "# [WATCH-1:Task] Do some stuff"
        new Issue(key: "WATCH-1", type: "Task", summary: "Do some stuff")                   | "# [WATCH-1:Task] Do some stuff"
    }

    def "Issue would be written to two level schema"() {
        when:
        def issue = new Issue(project: "OKR", type: "Story", summary: "Do some stuff", children: [
                new Issue(project: "WATCH", type: "Task", summary: "Prepare to do one thing"),
                new Issue(project: "WATCH", type: "Task", summary: "Actually do one thing"),
        ])
        then:
        SchemaWriter.toString(issue) == schema
        where:
        schema = """# [OKR:Story] Do some stuff
- [WATCH:Task] Prepare to do one thing
- [WATCH:Task] Actually do one thing"""
    }

    def "Issue would be written to three level schema"() {
        when:
        def issue = new Issue(project: "OKR", type: "Story", summary: "Do some stuff", children: [
                new Issue(project: "WATCH", type: "Task", summary: "Prepare to do one thing", children: [
                        new Issue(project: "WATCH", type: "Task", summary: "Prepare to do one thing part 1"),
                        new Issue(project: "WATCH", type: "Task", summary: "Prepare to do one thing part 2"),
                ]),
                new Issue(project: "WATCH", type: "Task", summary: "Actually do one thing"),
        ])
        then:
        SchemaWriter.toString(issue) == schema
        where:
        schema = """# [OKR:Story] Do some stuff
- [WATCH:Task] Prepare to do one thing
- - [WATCH:Task] Prepare to do one thing part 1
- - [WATCH:Task] Prepare to do one thing part 2
- [WATCH:Task] Actually do one thing"""
    }

    @Unroll
    def "Issue would be written to multi level schema"() {
        when:
        def issue = read(fromFile("jam/" + jsonFile), Issue)
        def schema = SchemaWriter.toString(issue)
        then:
        fromFile("jam/" + jamFile) == schema
        where:
        jamFile                 | jsonFile
        "new-multilevel.md"     | "new-multilevel.json"
        "existed-multilevel.md" | "existed-multilevel.json"
    }

}
