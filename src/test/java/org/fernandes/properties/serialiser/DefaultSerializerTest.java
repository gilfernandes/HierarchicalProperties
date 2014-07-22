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
import org.fernandes.properties.HierarchicalProperties;
import org.fernandes.properties.HierarchicalPropertiesFactoryTest;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author onepoint
 */
public class DefaultSerializerTest {

    /**
     * The factory for some hierarchical properties.
     */
    private HierarchicalPropertiesFactoryTest factory;
    
    /**
     * The default serialiser.
     */
    private DefaultSerialiser defaultSerializer;

    public DefaultSerializerTest() {
        defaultSerializer = new DefaultSerialiser();
    }

    @Before
    public void setUp() {
        factory = new HierarchicalPropertiesFactoryTest();
    }

    @Test
    public void serialize1() {
        HierarchicalProperties properties = factory.createSample();
        Assert.assertNotNull("Properties are null", properties);
        defaultSerializer = new DefaultSerialiser();
        final String sampleFile = "sample.txt";
        try(Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sampleFile), "UTF-8"))) {
            defaultSerializer.serialize(properties, fileWriter);
            Assert.assertTrue("Sample file is empty", new File(sampleFile).length() == 0);
        } catch (IOException ex) {
            Logger.getLogger(DefaultSerializerTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Cannot write file: " + ex);
        }
    }
}
