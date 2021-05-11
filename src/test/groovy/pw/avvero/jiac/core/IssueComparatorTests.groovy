package pw.avvero.jiac.core

import pw.avvero.jiac.core.IssueComparator
import pw.avvero.jiac.schema.Issue
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class IssueComparatorTests extends Specification {

    @Shared
    def comparator = new IssueComparator()

    @Unroll
    def "Comparison between #from and #to ends with error"() {
        when:
        comparator.compare(from, to)
        then:
        def e = thrown(IssueComparisonException)
        e.message == message
        where:
        from                      | to                        || message
        null                      | null                      || "Can't compare empty issues"
        null                      | new Issue()               || "Can't compare empty issues"
        new Issue()               | null                      || "Can't compare empty issues"
        new Issue()               | new Issue()               || "Can't compare keyless issues"
        new Issue()               | new Issue(key: "WATCH-1") || "Can't compare keyless issues"
        new Issue(key: "WATCH-1") | new Issue()               || "Can't compare keyless issues"
        new Issue(key: "WATCH-1") | new Issue(key: "WATCH-2") || "Issues has different keys"
    }

}
