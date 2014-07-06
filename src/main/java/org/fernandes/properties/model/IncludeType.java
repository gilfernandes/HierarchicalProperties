/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fernandes.properties.util.CPHandler;
import org.fernandes.properties.util.IOOperations;

/**
 * The type of include with methods for extracting from URLs.
 */
public enum IncludeType implements IncludeProcessor {

    /**
     * File include.
     */
    FILE("file") {
                /**
                 * Reads a file into a character sequence.
                 *
                 * @param url The URL of the file.
                 * @return the characters in the file represented by the URL.
                 */
                @Override
                public CharSequence process(URL url) {
                    try {
                        Path p = Paths.get(url.toURI());
                        byte[] bytes = Files.readAllBytes(p);
                        return new String(bytes, "UTF-8");
                    } catch (URISyntaxException | IOException ex) {
                        Logger.getLogger(IncludeType.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException(ex);
                    }
                }
            },
    /**
     * Classpath include.
     */
    CLASSPATH("classpath") {
                /**
                 * Reads a file from the classloader.
                 *
                 * @param uri The URL of the file to read.
                 * @return The characters from a file in the classpath.
                 */
                @Override
                public CharSequence process(URL uri) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(uri.openStream()))) {
                        return IOOperations.INSTANCE.copy(reader);
                    } catch (IOException ex) {
                        Logger.getLogger(IncludeType.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException(ex);
                    }
                }

            },
    /**
     * HTTP type of include.
     */
    HTTP("http") {
                @Override
                public CharSequence process(URL uri) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(uri.openStream()))) {
                        return IOOperations.INSTANCE.copy(reader);
                    } catch (IOException ex) {
                        Logger.getLogger(IncludeType.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException(ex);
                    }
                }
            };

    /**
     * The prefix used in the sources.
     */
    String prefix;

    /**
     * Associates this type to a prefix.
     *
     * @param prefix The prefix to be associated to this file.
     */
    private IncludeType(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the prefix associated to this include type.
     *
     * @return the prefix associated to this include type.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns the include prefix type by string.
     *
     * @param includeText The prefix representation to be matched to a type.
     * @return the include prefix type by string.
     */
    public static IncludeType byPrefix(String includeText) {
        for (IncludeType it : IncludeType.values()) {
            if (includeText.equals(it.getPrefix())) {
                return it;
            }
        }
        throw new IllegalArgumentException(String.format("Could not find prefix type for %s.", includeText));
    }

    /**
     * Processes a URI returning its content as a string.
     *
     * @param urlStr The URI as string to be processed.
     * @return the character sequence.
     */
    @Override
    public CharSequence process(String urlStr) {
        try {
            String composedStr = urlStr.contains("://") ? urlStr : String.format("%s://%s", this.prefix, urlStr);
            composedStr = composedStr.replaceAll("(\\:\\/)\\/{2,}", "$1/");
            switch (this) {
                case CLASSPATH:
                    return process(new URL(null, composedStr, new CPHandler()));
                default:
                    return process(new URL(composedStr));
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(IncludeType.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
