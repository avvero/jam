package pw.avvero.test;

import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;

import java.io.File;
import java.io.IOException;

public class ResourceDataProvider {

    public static String fromFile(String testResourceFile) throws IOException {
        return IOGroovyMethods.getText(ResourceGroovyMethods.newReader(new File("src/test/resources/" + testResourceFile)));
    }

}
