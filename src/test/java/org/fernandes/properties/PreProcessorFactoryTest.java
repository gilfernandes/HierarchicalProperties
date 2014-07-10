/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.fernandes.properties.model.Node;
import org.junit.Test;

/**
 * Test for the preprocessor factory.
 *
 * @author onepoint
 */
//@Ignore
public class PreProcessorFactoryTest {

    /**
     * Creates an instance of the preprocessor factory and performs the includes
     * from the classpath.
     */
    @Test
    public void createInstanceAndIncludeClasspath() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_include_cp.txt"));
            sampleChecks(included);
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }
    
    /**
     * Creates an instance of the preprocessor factory and performs the includes
     * from the classpath.
     */
    @Test
    public void createInstanceAndIncludeFile() throws URISyntaxException, MalformedURLException {
        
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_include_file.txt"));
            sampleChecks(included);
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }

    /**
     * Checks, if nodes are found and if the number of children in one of them
     * is correct.
     *
     * @param included The string with all the includes in it.
     */
    private void sampleChecks(String included) {
        Assert.assertNotNull("Included is null", included);
        System.out.println(included);
        HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
        Node root = props.getNode("/");
        Assert.assertNotNull("The root node is null", root);
        Node testNode = props.getNode("/Test");
        Assert.assertNotNull("Test node is null", testNode);
        Map<String, String> children = testNode.getPropertyMap();
        Assert.assertTrue("Test node should have one child at least one child", children.size() > 0);
    }
}
