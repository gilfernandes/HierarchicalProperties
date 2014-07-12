/*
 OSSCUBE 2014
 */

package org.fernandes.properties;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fernandes.properties.model.DefaultNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the hierarchical properties factory.
 * @author onepoint
 */
//@Ignore
public class HierarchicalPropertiesFactoryTest {
    
    /**
     * Tests a parsing hierarchical properties with de-referencing.
     */
    @Test
    public void testHierarchicalPropertiesFactory() {
        try {
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_sample.txt"), true);
            DefaultNode helloNode = props.getNode("/Test/hello");
            Assert.assertNotNull("/Test/hello is null", helloNode);
            final String propsStr = props.toString();
            Assert.assertTrue("System dereference failed", propsStr.contains("System32"));
            System.out.println(propsStr);
            Assert.assertTrue(String.format("Node %s does not contain a line format.", helloNode), helloNode.sizeMultilineComment() > 0);
            helloNode.iteratorMultilineComment().forEachRemaining((c) -> System.out.println("multiline :::" + c));
            helloNode.iteratorLineComment().forEachRemaining((c) -> System.out.println("single line:::" + c));
            
            DefaultNode fernandesNode = props.getNode("/org/fernandes/properties/test");
            Assert.assertNotNull("Fernandes node not found", fernandesNode);
            fernandesNode.getChildren();
        } catch (IOException ex) {
            Logger.getLogger(HierarchicalPropertiesFactoryTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.toString());
        }
    }
}
