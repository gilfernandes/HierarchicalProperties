/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

import org.fernandes.properties.model.PreProcessorContainer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.fernandes.properties.model.IncludeType;
import org.fernandes.properties.parser.PreProcessorParser;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

/**
 * Factory for the preprocessor parser/
 *
 * @author onepoint
 */
public class PreProcessorFactory {
    
    /**
     * Creates an instance of the properties from the classpath.
     * @param cp The classpath to read from.
     * @return a string with the preprocessed properties.
     */
    public static String createInstanceFromCp(String cp) {
        if(cp == null || cp.trim().isEmpty()) {
            throw new IllegalArgumentException("The classpath is empty.");
        }
        CharSequence content = IncludeType.CLASSPATH.process(cp);
        return createInstance(content.toString());
    }

    /**
     * Parses a file for processing the includes.
     *
     * @param path The urlContent to the file to be processed.
     * @return the result of the inclusions based on the directives in
     * {@code urlContent}.
     */
    public static String createInstance(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(String.format("%s does not exist.", path));
        }
        String content;
        try {
            content = new String(Files.readAllBytes(path), "UTF-8");
            return createInstance(content);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Could not read and process %s.", path), ex);
        }
    }

    /**
     * Creates the instance from a string.
     *
     * @param input The parser input.
     * @return the string with all the inclusions made.
     */
    public static String createInstance(final String input) {
        PreProcessorParser parser = Parboiled.createParser(PreProcessorParser.class);
        ParsingResult<PreProcessorContainer> result = new RecoveringParseRunner<PreProcessorContainer>(
                parser.main()).run(input);
        final PreProcessorContainer resultValue = result.resultValue;
        return resultValue == null ? "" : resultValue.getPreprocessedText();
    }
}
