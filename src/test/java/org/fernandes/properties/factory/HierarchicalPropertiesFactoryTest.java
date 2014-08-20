/*
 OSSCUBE 2014
 */

package org.fernandes.properties.factory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fernandes.properties.model.HierarchicalProperties;
import org.fernandes.properties.model.PropertyNode;
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
        createSample("src/test/resources/hierarchicalProperties/map_sample.txt");
    }

    /**
     * Tests a parsing hierarchical properties with de-referencing from the classpath.
     */
    @Test
    public void testHierarchicalPropertiesFactoryCp() {
        try {
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstanceCp("hierarchicalProperties/map_sample.txt");
            Assert.assertNotNull("The properties are null", props);
        } catch (IOException ex) {
            Logger.getLogger(HierarchicalPropertiesFactoryTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.toString());
        }
    }

    /**
     * Tests a parsing hierarchical properties with de-referencing from the classpath.
     */
    @Test
    public void testHierarchicalPropertiesEmpty() {
        try {
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstanceCp("hierarchicalProperties/map_sample_empty.txt");
            Assert.assertNotNull("The properties are null", props);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNotNull("test node is null", testNode);
            String keydef = testNode.getProperty("keyDef");
            Assert.assertNotNull("keyDef is null", keydef);
            Assert.assertEquals("prod_val", keydef);
        } catch (IOException ex) {
            Logger.getLogger(HierarchicalPropertiesFactoryTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.toString());
        }
    }

    /**
     * Tests the sample hierarchical properties.
     * @param sample The sample file.
     * @return the sample hierarchical properties.
     */
    public HierarchicalProperties createSample(final String sample) {
        try {
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(Paths.get(sample), true, false);
            PropertyNode helloNode = props.getNode("/Test/hello");
            Assert.assertNotNull("/Test/hello is null", helloNode);
            final String propsStr = props.toString();
            Assert.assertTrue("System dereference failed", propsStr.contains("System32"));
            System.out.println(propsStr);
            Assert.assertTrue(String.format("Node %s does not contain a line format.", helloNode), helloNode.sizeMultilineComment() > 0);
            helloNode.iteratorMultilineComment().forEachRemaining((c) -> System.out.println("multiline :::" + c));
            helloNode.iteratorLineComment().forEachRemaining((c) -> System.out.println("single line:::" + c));

            PropertyNode fernandesNode = props.getNode("/org/fernandes/properties/test");
            Assert.assertNotNull("Fernandes node not found", fernandesNode);
            fernandesNode.getChildren();
            return props;
        } catch (IOException ex) {
            Logger.getLogger(HierarchicalPropertiesFactoryTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.toString());
        }
        return null;
    }
}
