/*
 OSSCUBE 2014
 */
package org.fernandes.properties.factory;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.fernandes.properties.model.HierarchicalProperties;
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

    /**
     * Tests a parsing hierarchical properties with de-referencing from the classpath.
     */
    @Test
    public void testTypes() {
        try {
            HierarchicalProperties props = HierarchicalPreprocessorFactory.createInstanceCp("hierarchicalProperties/map_if_types.txt");
            org.junit.Assert.assertNotNull("The properties are null", props);
            PropertyNode testProps = props.getNode("/Test");
            Assert.assertNotNull("Test is null", props);
            Boolean keyBoolean = testProps.getPropertyAsBoolean("keyBoolean");
            Assert.assertNotNull("keyBoolean is null", keyBoolean);
            Assert.assertTrue("keyBoolean is not true", keyBoolean);
            Double keyDouble = testProps.getPropertyAsDouble("keyDouble");
            Assert.assertNotNull("keyDouble is null", keyDouble);
            Assert.assertTrue("keyDouble is not 2.345", keyDouble == 2.345);
            Integer keyInt = testProps.getPropertyAsInt("keyInt");
            Assert.assertNotNull("keyInt is null", keyInt);
            Assert.assertTrue("keyInt is not 123", keyInt == 123);
        } catch (Exception ex) {
            Logger.getLogger(HierarchicalPropertiesFactoryTest.class.getName()).log(Level.SEVERE, null, ex);
            org.junit.Assert.fail(ex.toString());
        }
    }


}