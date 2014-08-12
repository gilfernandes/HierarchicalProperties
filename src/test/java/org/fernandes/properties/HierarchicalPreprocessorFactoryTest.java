/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

import org.fernandes.properties.model.HierarchicalProperties;
import org.fernandes.properties.factory.HierarchicalPreprocessorFactory;
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

    /**
     * Preprocesses and instantiates the hierarchical properties.
     */
    @Test
    public void factoryMapIf() {
        HierarchicalProperties props = HierarchicalPreprocessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_2.txt"));
        PropertyNode testNode = props.getNode("/Test");
        Assert.assertNotNull("testNode is null", testNode);
        String key3 = testNode.getProperty("key3");
        Assert.assertNotNull("key3 is null", key3);
    }
    
    /**
     * Preprocesses and instantiates the hierarchical properties.
     */
    @Test
    public void factoryMapIfNested() {
        HierarchicalProperties props = HierarchicalPreprocessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_nested.txt"));
        PropertyNode testNode = props.getNode("/Test");
        Assert.assertNotNull("testNode is null", testNode);
        String key3 = testNode.getProperty("key3");
        Assert.assertNotNull("key3 is null", key3);
    }
    
    /**
     * Preprocesses and instantiates the hierarchical properties.
     */
    @Test
    public void factoryMapIfElseNested() {
        HierarchicalProperties props = HierarchicalPreprocessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_else_nested.txt"));
        PropertyNode testNode = props.getNode("/Test");
        Assert.assertNotNull("testNode is null", testNode);
        String key3 = testNode.getProperty("key3");
        Assert.assertNotNull("key3 is null", key3);
        String keyDef2 = testNode.getProperty("keyDef2");
        Assert.assertNull("keyDef2 is not null", keyDef2);
    }
    
    
}