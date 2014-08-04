/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

import java.nio.file.Paths;
import junit.framework.Assert;
import org.fernandes.properties.model.PropertyNode;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the factory which preprocesses and materialises the hierarchical
 * properties.
 *
 * @author onepoint
 */
public class HierarchicalPreprocessorFactoryTest {

    public HierarchicalPreprocessorFactoryTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void factory1() {
        
        HierarchicalProperties props = HierarchicalPreprocessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_2.txt"));
        PropertyNode testNode = props.getNode("/Test");
        Assert.assertNotNull("testNode is null", testNode);
        String key3 = testNode.getProperty("key3");
        Assert.assertNotNull("key3 is null", key3);
    }
}