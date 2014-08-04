/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.fernandes.properties.model.PropertyNode;
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
    public void createInstanceCpAndIncludeClasspath() {
        try {
            String included = PreProcessorFactory.createInstanceFromCp("hierarchicalProperties/map_include_cp.txt");
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
    public void createInstanceCpAndIncludeClasspathWithDef() {
        try {
            String included = PreProcessorFactory.createInstanceFromCp("hierarchicalProperties/map_define_1.txt");
            sampleChecks(included);
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }

    /**
     * Creates an instance of the preprocessor factory and performs the includes
     * from the classpath, checks if the if condition works.
     */
    @Test
    public void createInstanceCpAndIncludeClasspathWithDefIf1() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_1.txt"));
            Assert.assertFalse("if not resolved", included.contains("if:env == prod"));
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            PropertyNode root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNotNull("Test node is null", testNode);
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }
    
    /**
     * Creates an instance of the preprocessor factory and performs the includes
     * from the classpath, checks if the if condition works.
     */
    @Test
    public void createInstanceCpAndIncludeClasspathWithDefIf2() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_2.txt"));
            Assert.assertFalse("if not resolved", included.contains("if:env == prod"));
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            PropertyNode root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNotNull("Test node is null", testNode);
            String key3 = testNode.getProperty("key3");
            Assert.assertNotNull("key3 is null", key3);
            String keyDef = testNode.getProperty("keyDef");
            Assert.assertNull("keyDef is not null", keyDef);
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }
    
    /**
     * Creates an instance of the preprocessor factory and performs the includes
     * from the classpath, checks if the if condition works.
     */
    @Test
    public void createInstanceCpAndIncludeClasspathWithDefIfNot() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_not_if_1.txt"));
            Assert.assertFalse("if not resolved", included.contains("if:env == prod"));
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            PropertyNode root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNull("Test node is not null", testNode);
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
    public void createInstanceAndIncludeFile() {

        try {

            String include = createIncludeTag();

            List<String> lines = Files.readAllLines(Paths.get("src/test/resources/hierarchicalProperties/map_include_file.txt"));
            lines.add(0, include);
            System.out.println(lines);

            Path tempFile = Files.createTempFile("hpropr_", ".hproperties");
            Files.write(tempFile, lines, Charset.forName("UTF-8"));

            String included = PreProcessorFactory.createInstance(tempFile);
            sampleChecks(included);

            Assert.assertTrue("Could not find include1", included.contains("includeKey1"));
            Assert.assertTrue("Could not find include2", included.contains("includeKey2"));
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }

    /**
     * Simply includes from http.
     */
    @Test
    public void includeFromHttp() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_include_http.txt"));
            sampleChecks(included);
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }

    /**
     * Generates the include tag from a relative path. We cannot at present
     * include directly relative paths in the include directory.
     *
     * @return an include preprocessor directive.
     */
    private String createIncludeTag() {
        Path p = Paths.get("src/test/resources/hierarchicalProperties/include1.txt");
        Assert.assertTrue(String.format("Path %s does not exist.", p), Files.exists(p));
        URI uri = p.toUri();
        String include = String.format("!<%s>", uri);
        return include;
    }

    /**
     * Checks, if nodes are found and if the number of children in one of them
     * is correct.
     *
     * @param included The string with all the includes in it.
     */
    private void sampleChecks(String included) {
        Assert.assertTrue("'classpath:' should not be in included", !included.contains("classpath:"));
        Assert.assertNotNull("Included is null", included);
        System.out.println(included);
        HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
        PropertyNode root = props.getNode("/");
        Assert.assertNotNull("The root node is null", root);
        PropertyNode testNode = props.getNode("/Test");
        Assert.assertNotNull("Test node is null", testNode);
        Map<String, String> children = testNode.getPropertyMap();
        Assert.assertTrue("Test node should have one child at least one child", children.size() > 0);
        String val2 = root.getProperty("key2");
        Assert.assertFalse("The value for key2 should not be null", val2 == null);
    }
}
