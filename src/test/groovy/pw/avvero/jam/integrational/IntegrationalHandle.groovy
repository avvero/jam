package pw.avvero.jam.integrational

import org.junit.Ignore
import pw.avvero.jam.IssueFileDataProvider
import pw.avvero.jam.jira.dto.JiraIssue
import pw.avvero.jam.jira.JiraIssueMapper
import pw.avvero.jam.schema.SchemaWriter
import pw.avvero.jam.terminal.ConsoleWriter
import spock.lang.Specification

import static pw.avvero.jam.core.SerializationUtils.read
import static pw.avvero.test.ResourceDataProvider.fromFile

@Ignore
class IntegrationalHandle extends Specification {

    def "Parse jira issue file and print schema to sout"() {
        when:
        def issue = JiraIssueMapper.map(read(fromFile(file), JiraIssue), null)
        def schema = SchemaWriter.toString(issue)
        new ConsoleWriter().newLineBlue(schema)
        then:
        1 == 1
        where:
        file = "jira/xxx.json"
    }

    def "Parse jira issue files and print schema to sout"() {
        when:
        def provider = new IssueFileDataProvider("jira/xxx")
        def issue = provider.getWithChildren("WATCH-1000")
        def schema = SchemaWriter.toString(issue)
        new ConsoleWriter().newLineBlue(schema)
        then:
        1 == 1
    }

}
