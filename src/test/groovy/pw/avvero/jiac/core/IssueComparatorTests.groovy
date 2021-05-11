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
    def "Comparison between #from and #to is #result"() {
        expect:
        comparator.compare(from, to) == result
        where:
        from        | to          || result
        null        | null        || []
        null        | new Issue() || []
        new Issue() | null        || []
    }

}
