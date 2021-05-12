package pw.avvero.jam

import pw.avvero.jam.App
import spock.lang.Specification

class AppTests extends Specification {

    def "Run app without files with schema ends with exception"() {
        when:
        App.main()
        then:
        thrown(IllegalArgumentException)
    }

}
