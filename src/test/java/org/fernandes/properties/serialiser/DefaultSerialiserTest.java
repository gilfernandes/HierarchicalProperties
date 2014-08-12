/*
 OSSCUBE 2014
 */
package org.fernandes.properties.serialiser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.fernandes.properties.model.HierarchicalProperties;
import org.fernandes.properties.factory.HierarchicalPropertiesFactoryTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the serialiser.
 * @author onepoint
 */
public class DefaultSerialiserTest {

    /**
     * The factory for some hierarchical properties.
     */
    private HierarchicalPropertiesFactoryTest factory;
    
    /**
     * The default serialiser.
     */
    private DefaultSerialiser defaultSerializer;

    public DefaultSerialiserTest() {
        defaultSerializer = new DefaultSerialiser();
    }

    @Before
    public void setUp() {
        factory = new HierarchicalPropertiesFactoryTest();
    }

    /**
     * Creates hierarchical properties, serialises it and then re-creates the
     * properties from the serialisation and does some checks.
     */
    @Test
    public void serialize1() {
        HierarchicalProperties properties = factory.createSample("src/test/resources/hierarchicalProperties/map_sample.txt");
        Assert.assertNotNull("Properties are null", properties);
        defaultSerializer = new DefaultSerialiser();
        final String sampleFile = "sample.txt";
        try(Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sampleFile), "UTF-8"))) {
            defaultSerializer.serialize(properties, fileWriter);
            Assert.assertTrue("Sample file is empty", new File(sampleFile).length() == 0);
            HierarchicalProperties properties2 = factory.createSample("src/test/resources/hierarchicalProperties/map_sample.txt");
            Assert.assertTrue("Node count is 0", properties2.nodeCount() > 0);
        } catch (IOException ex) {
            Logger.getLogger(DefaultSerialiserTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Cannot write file: " + ex);
        }
    }
}
