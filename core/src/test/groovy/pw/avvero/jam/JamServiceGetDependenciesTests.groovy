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
    graph [nodesep="0.1"];
    rankdir=LR;
    node [shape=box, penwidth=1];
    "WATCH-6" [shape=box, color=green, label=<WATCH-6<BR/><FONT POINT-SIZE="10">Very first enabler</FONT>>, URL="dependencies?issueCode=WATCH-6"];
    "WATCH-1" [shape=box, color=green, label=<WATCH-1<BR/><FONT POINT-SIZE="10">Working with jira issues as a code</FONT>>, URL="dependencies?issueCode=WATCH-1"];
    "WATCH-2" [shape=box, color=green, label=<WATCH-2<BR/><FONT POINT-SIZE="10">Prepare to do one thing</FONT>>, URL="dependencies?issueCode=WATCH-2"];
    "WATCH-3" [shape=box, color=green, label=<WATCH-3<BR/><FONT POINT-SIZE="10">Prepare to do one thing part 1</FONT>>, URL="dependencies?issueCode=WATCH-3"];
    "WATCH-4" [shape=box, color=green, label=<WATCH-4<BR/><FONT POINT-SIZE="10">Prepare to do one thing part 2</FONT>>, URL="dependencies?issueCode=WATCH-4"];
    "WATCH-5" [shape=box, color=green, label=<WATCH-5<BR/><FONT POINT-SIZE="10">Actually do one thing</FONT>>, URL="dependencies?issueCode=WATCH-5"];
    "WATCH-1" -> "WATCH-2" [color=grey];
    "WATCH-2" -> "WATCH-3" [color=grey];
    "WATCH-2" -> "WATCH-4" [color=grey];
    "WATCH-2" -> "WATCH-5" [style=dashed, label="blocks", color=red];
    "WATCH-2" -> "WATCH-6" [style=dashed, label="is blocked by", color=orange];
    "WATCH-1" -> "WATCH-5" [color=grey];
    "WATCH-5" -> "WATCH-2" [style=dashed, label="is blocked by", color=orange];
}"""
    }

    def "Method getDependenciesForIssueWithChildren returns schema without children for empty epic"() {
        when:
        def schema = service.getDependenciesForIssueWithChildren("WATCH-6")
        then:
        schema == """digraph jam {
    graph [nodesep="0.1"];
    rankdir=LR;
    node [shape=box, penwidth=1];
    "WATCH-6" [shape=box, color=green, label=<WATCH-6<BR/><FONT POINT-SIZE="10">Empty epic</FONT>>, URL="dependencies?issueCode=WATCH-6"];
}"""
    }

}
