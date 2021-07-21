package pw.avvero.jam

import pw.avvero.jam.IssueFileDataProvider
import pw.avvero.jam.JamService
import spock.lang.Shared
import spock.lang.Specification

class JamServiceGetDependenciesTests extends Specification {

    @Shared
    def service = new JamService(new IssueFileDataProvider("jira/file-api"))

    def "Method getDependenciesForIssueWithChildren returns graphviz schema with dependencies"() {
        when:
        def schema = service.getDependenciesForIssueWithChildren("WATCH-1")
        then:
        schema == """digraph jam {
    "WATCH-1";
    { rank=same "WATCH-2" "WATCH-5" }
    "WATCH-1" -> "WATCH-2";
    { rank=same "WATCH-3" "WATCH-4" }
    "WATCH-2" -> "WATCH-3";
    "WATCH-2" -> "WATCH-4";
    "WATCH-1" -> "WATCH-5";
    "WATCH-2" -> "WATCH-5";
    "WATCH-6" -> "WATCH-2";
}"""
    }

    def "Method getDependenciesForIssueWithChildren returns schema without children for empty epic"() {
        when:
        def schema = service.getDependenciesForIssueWithChildren("WATCH-6")
        then:
        schema == """# [WATCH-6:Epic] Empty epic"""
    }

}
