/*
 OSSCUBE 2014
 */
package org.fernandes.properties.factory;

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
import org.fernandes.properties.model.HierarchicalProperties;
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
    public void createInstanceCpAndIncludeClasspathWithDefIfElse() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_else.txt"));
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
    public void createInstanceCpAndIncludeClasspathWithDefIfElsenNested() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_else_nested.txt"));
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
            String keyDef2 = testNode.getProperty("keyDef2"); // Should still be null
            Assert.assertNull("keyDef2 is not null", keyDef2);
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
    public void createInstanceCpAndIncludeClasspathWithDefIfNested() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_nested.txt"));
            Assert.assertFalse("if 1 not resolved", included.contains("if:env == prod"));
            Assert.assertFalse("if 2 not resolved", included.contains("if:system == sys1"));
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            PropertyNode root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNotNull("Test node is null", testNode);
            String key3 = testNode.getProperty("key3");
            Assert.assertNotNull("key3 is null", key3);
            String key4 = testNode.getProperty("key4");
            Assert.assertNull("key4 is not null", key4);
            String key5 = testNode.getProperty("key5");
            Assert.assertNotNull("key5 is null", key5);
            String keyDef = testNode.getProperty("keyDef");
            Assert.assertNull("keyDef is not null", keyDef);
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }

    /**
     * Creates an instance of the preprocessor factory and performs the includes
     * from the classpath, checks if the if conditions work.
     */
    @Test
    public void createInstanceCpAndIncludeClasspathWithDefIfElseIf() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_if_else_elseif.txt"));
            Assert.assertFalse("if 1 not resolved", included.contains("if:env == staging"));
            Assert.assertFalse("elseif not resolved", included.contains("elseif:env == staging"));
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            PropertyNode root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNotNull("Test node is null", testNode);
            String key3 = testNode.getProperty("key3");
            Assert.assertNull("key3 is not null", key3);
            String keyStaging = testNode.getProperty("keyStaging");
            Assert.assertNotNull("keyStaging is null", keyStaging);
            String keyDef = testNode.getProperty("keyDef");
            Assert.assertNull("keyDef is not null", keyDef);
            String keyDef2 = testNode.getProperty("keyDef2");
            Assert.assertNull("keyDef2 is not null", keyDef2);
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
     * from the classpath, checks if the if condition works.
     */
    @Test
    public void createInstanceDefineExternalVars() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_define_external_vars.txt"));
            Assert.assertTrue("key2 has not been found in preprocessed string.", included.contains("key2"));
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            PropertyNode root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNotNull("Test node is null", testNode);
            String keyDef = testNode.getProperty("keyDef");
            Assert.assertNotNull("keyDef is null", keyDef);
            Assert.assertEquals("keyDef is not 'prod_val'", "prod_val", keyDef);
            String keyPath = testNode.getProperty("keyPath");
            Assert.assertNotNull("key path is null", keyPath);
            Assert.assertTrue("keyPath contains #", !keyPath.contains("#"));
            String keyOsName = testNode.getProperty("keyOsName");
            Assert.assertFalse("keyOsName contains SYS", keyOsName.contains("SYS"));
        } catch (Exception e) {
            Logger.getLogger(PreProcessorFactoryTest.class.getName()).log(Level.SEVERE, "Test fails", e);
            Assert.fail(e.toString());
        }
    }

    /**
     * Testing the for clause.
     */
    @Test
    public void createInstanceFor() {
        try {
            String included = PreProcessorFactory.createInstance(Paths.get("src/test/resources/hierarchicalProperties/map_for.txt"));
            Assert.assertTrue("key2 has not been found in preprocessed string.", included.contains("key2"));
            Assert.assertTrue("'sequenceFor = testFor_prod_9' not in string", included.contains("testFor_prod_10"));
            Assert.assertTrue("'sequenceFor = testFor_prod_10' not in string", included.contains("testFor_prod_10"));
            Assert.assertTrue("'SYSTEMROOT: C:\\Windows - OS_NAME: Windows 7' was not found.",
                    included.contains("SYSTEMROOT: C:\\Windows - OS_NAME: Windows 7"));
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
            PropertyNode root = props.getNode("/");
            Assert.assertNotNull("The root node is null", root);
            PropertyNode testNode = props.getNode("/Test");
            Assert.assertNotNull("Test node is null", testNode);
            String keyDef = testNode.getProperty("keyDef");
            Assert.assertNotNull("keyDef is null", keyDef);
            Assert.assertEquals("keyDef is not 'prod_val'", "prod_val", keyDef);
            String keyPath = testNode.getProperty("keyPath");
            Assert.assertNotNull("key path is null", keyPath);
            Assert.assertTrue("keyPath contains #", !keyPath.contains("#"));
            String keyOsName = testNode.getProperty("keyOsName");
            Assert.assertFalse("keyOsName contains SYS", keyOsName.contains("SYS"));
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
