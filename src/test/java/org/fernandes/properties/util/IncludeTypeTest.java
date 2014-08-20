/*
 OSSCUBE 2014
 */
package org.fernandes.properties.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.fernandes.properties.model.IncludeType;
import org.junit.Before;
import org.junit.Test;

/**
 * Include type tester.
 *
 * @author onepoint
 */
public class IncludeTypeTest {

    public IncludeTypeTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Tests the method for including files.
     * @throws java.net.MalformedURLException
     */
    @Test
    public void fileInclude() throws MalformedURLException {
        Path curPath = Paths.get("src/test/resources/hierarchicalProperties/map_sample.txt");
        final IncludeType FILE = IncludeType.FILE;
        checkInclude(FILE, curPath.toUri().toURL());
    }

    /**
     * Tests the method for including from HTTP.
     * @throws java.net.MalformedURLException
     */
    @Test
    public void httpInclude() throws MalformedURLException {
        URL url = new URL("http://docs.oracle.com/javase/7/docs/api/java/nio/Buffer.html#flip()");
        checkInclude(IncludeType.HTTP, url);
    }

    /**
     * Tests the method for including from HTTP.
     * @throws java.net.MalformedURLException
     */
    @Test
    public void cpInclude() throws MalformedURLException {
        URL url = new URL(null, "classpath://hierarchicalProperties/map_sample.txt", new CPHandler());
        checkInclude(IncludeType.CLASSPATH, url);
    }

    /**
     * Checks, if the includes have worked.
     * @param includeType The type to include.
     * @param url The URL to be extracted from.
     */
    private void checkInclude(final IncludeType includeType, URL url) {
        try {
            CharSequence content = includeType.process(url);
            Assert.assertFalse("There is no content", content == null || content.toString().isEmpty());
            System.out.println(content);
        } catch (Exception ex) {
            Logger.getLogger(IncludeTypeTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.toString());
        }
    }
}
