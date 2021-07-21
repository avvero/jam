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
    rankdir=LR;
    node [shape=box];
    "WATCH-1" [shape=Mdiamond];
    "WATCH-1" -> "WATCH-2";
    "WATCH-2" -> "WATCH-3";
    "WATCH-2" -> "WATCH-4";
    "WATCH-2" -> "WATCH-5" [style=dashed, label="blocks", color=red];
    "WATCH-2" -> "WATCH-6" [style=dashed, label="is blocked by", color=orange];
    "WATCH-1" -> "WATCH-5";
    "WATCH-5" -> "WATCH-2" [style=dashed, label="is blocked by", color=orange];
}"""
    }

    def "Method getDependenciesForIssueWithChildren returns schema without children for empty epic"() {
        when:
        def schema = service.getDependenciesForIssueWithChildren("WATCH-6")
        then:
        schema == """digraph jam {
    rankdir=LR;
    node [shape=box];
    "WATCH-6" [shape=Mdiamond];
}"""
    }

}
