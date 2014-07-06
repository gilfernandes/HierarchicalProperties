/*
 OSSCUBE 2014
 */

package org.fernandes.properties;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.fernandes.properties.model.Node;
import org.junit.Test;

/**
 * Test for the preprocessor factory.
 * @author onepoint
 */
public class PreProcessorFactoryTest {
    
    /**
     * Creates an instance of the preprocessor factory and performs the includes.
     */
    @Test
    public void createInstanceAndInclude() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_include.txt"));
            Assert.assertNotNull("Included is null", included);
            System.out.println(included);
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            Node root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
        }
        catch(Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }
}
