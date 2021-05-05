package pw.avvero.jiac.dsl


import pw.avvero.jiac.dsl.SchemaParser
import pw.avvero.jiac.entity.Issue
import spock.lang.Specification
import spock.lang.Unroll

class SchemaInvalidDataParsingTests extends Specification {

    @Unroll
    def "Schema is parsed from invalid schema string #schema"() {
        expect:
        new SchemaParser().parseFromString(schema) == issue
        where:
        schema           | issue
        "#[:]"           | new Issue()
        "#[TASK:] To do" | new Issue(project: "TASK", summary: "To do")
    }
}
