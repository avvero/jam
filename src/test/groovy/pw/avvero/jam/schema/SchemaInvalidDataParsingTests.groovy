package pw.avvero.jam.schema

import pw.avvero.jam.schema.Issue
import pw.avvero.jam.schema.SchemaParser
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

class SchemaInvalidDataParsingTests extends Specification {

    @Ignore("Not ready to provide it now")
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
