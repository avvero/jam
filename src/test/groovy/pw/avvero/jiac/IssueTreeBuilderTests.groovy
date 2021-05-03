package pw.avvero.jiac

import pw.avvero.jiac.dsl.Issue
import pw.avvero.jiac.dsl.IssueTreeBuilder
import pw.avvero.jiac.dsl.Pair
import spock.lang.Specification
import spock.lang.Unroll

class IssueTreeBuilderTests extends Specification {

    @Unroll
    def "Builder returns null if list is #list"() {
        expect:
        IssueTreeBuilder.build(list) == result
        where:
        list | result
        null | null
        []   | null
    }

    @Unroll
    def "Builder returns #result if list is #list"() {
        when:
        def issues = list.collect { e -> Pair.of(e[0], new Issue(summary: e[1])) }
        then:
        IssueTreeBuilder.build(issues).collect { i -> simplify(i) } == result
        where:
        list                           | result
        [[0, "1"]]                     | ["1": []]
        [[0, "1"], [0, "2"]]           | ["1": [], "2": []]
        [[0, "1"], [1, "2"], [0, "3"]] | ["1": [["2": []]], "3": []]
    }

    /**
     * Issue to map [summary: [[childSummary: [...]], ... ]]
     * @param issue
     * @return
     */
    def simplify(Issue issue) {
        def map = [:]
        map[issue.summary] = issue.children.collect { i -> simplify(i) }
        return map
    }
}
