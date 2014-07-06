/*
 OSSCUBE 2014
 */

package org.fernandes.properties.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Used to handle classpath URLs.
 * @author onepoint
 */
public class CPHandler extends URLStreamHandler {
    
    /** The classloader to find resources from. */
    private final ClassLoader classLoader;

    public CPHandler() {
        this.classLoader = getClass().getClassLoader();
    }

    public CPHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        final String path = u.getHost();
        final String file = u.getFile();
        final URL resourceUrl = classLoader.getResource(path + file);
        return resourceUrl.openConnection();
    }
}