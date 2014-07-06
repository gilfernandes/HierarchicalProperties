/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model;

import java.net.URL;

/**
 * Processes includes in different ways, but always returning a string.
 * @author onepoint
 */
public interface IncludeProcessor {
    
    /**
     * Processes a URI returning its content as a string.
     * @param uri The URI to be processed.
     * @return the character sequence.
     */
    public CharSequence process(URL uri);
    
   /**
    * Processes a URI returning its content as a string.
    * @param urlStr The URI as string to be processed.
    * @return the character sequence.
    */
    public CharSequence process(String urlStr);
    
}
