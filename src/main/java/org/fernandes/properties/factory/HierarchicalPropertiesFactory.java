/*
 OSSCUBE 2014
 */
package org.fernandes.properties.factory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.fernandes.properties.model.HierarchicalProperties;
import org.fernandes.properties.util.Reloader;
import org.fernandes.properties.model.DefaultHierarchicalProperties;
import org.fernandes.properties.model.IncludeType;
import org.fernandes.properties.parser.HierarchicalPropertiesParser;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

/**
 * Factory used to build hierarchical properties.
 *
 * @author onepoint
 */
public class HierarchicalPropertiesFactory {
    
    /**
     * Creates an instance of the hierarchical properties from a classpath resource.
     * @param classpath The classpath resource.
     * @return an instance of the hierarchical properties
     * @throws IOException In case the file cannot be found.
     */
    public static final HierarchicalProperties createInstanceCp(String classpath) throws IOException {
        CharSequence input = IncludeType.CLASSPATH.process(classpath);
        return createInstance(input.toString(), true);
    }

    /**
     * Creates an instance of the hierarchical properties which dereferences per default.
     * @param inputPath The input path.
     * @return an instance of the hierarchical properties
     * @throws IOException In case the hierarchical properties are found or not.
     */
    public static final HierarchicalProperties createInstance(Path inputPath) throws IOException {
        return createInstance(inputPath, true, false);
    }
    
    /**
     * Creates an instance of the hierarchical properties allowing the 
     * user to dereference or not.
     * @param inputPath The input path.
     * @param dereference If {@code true} the hierarchical properties references are dereferenced,
     * else not.
     * @param addReloader If {@code true} the hierarchical properties are reloaded
     * when the file is changed.
     * @return an instance of the hierarchical properties
     * @throws IOException In case the hierarchical properties are found or not.
     */
    public static final HierarchicalProperties createInstance(Path inputPath, boolean dereference, boolean addReloader) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Files.copy(inputPath, bout);
        final String input = bout.toString("UTF-8");
        HierarchicalProperties props = createInstance(input, dereference);
        if(addReloader) {
            Reloader.INSTANCE.startReloadThread(inputPath, props);
        }
        return props;
    }

    /**
     * Creates the instance from a string.
     * @param input The parser input.
     * @param dereference If {@code true} the hierarchical properties references are dereferenced,
     * else not.
     * @return  an instance of the hierarchical properties
     */
    public static HierarchicalProperties createInstance(final String input, boolean dereference) {
        HierarchicalPropertiesParser parser = Parboiled.createParser(HierarchicalPropertiesParser.class);
        ParsingResult<?> result = new RecoveringParseRunner<DefaultHierarchicalProperties>(
                parser.main()).run(input);
        final DefaultHierarchicalProperties resultValue = (DefaultHierarchicalProperties) result.resultValue;
        if(resultValue == null) {
            return null;
        }
        if(dereference) {
            resultValue.dereferenceRefs();
        }
        return resultValue;
    }
}
