/*
 OSSCUBE 2014
 */

package org.fernandes.properties;

import org.fernandes.properties.model.IncludeType;

/**
 * Container for the result of the pre processing of the hierarchical properties.
 * @author onepoint
 */
public class PreProcessorContainer {
    
    /**
     * The list with the includes.
     */
    private final StringBuilder preprocessedText = new StringBuilder();
    
    /**
     * The current include.
     */
    private IncludeType curIncludeType;
    
    /**
     * Adds normal text with no processing to the includeList.
     * @param text The text to be added to the include list.
     * @return a reference to this object.
     */
    public PreProcessorContainer processText(String text) {
        preprocessedText.append(text);
        return this;
    }
    
    /**
     * Sets the current include type.
     * @param text The text representing n include type.
     * @return a reference to this object.
     */
    public PreProcessorContainer processCurIncludeType(String text) {
        curIncludeType = IncludeType.byPrefix(text);
        return this;
    }
    
    /**
     * Adds normal text with no processing to the includeList.
     * @param text The text to be added to the include list.
     * @return a reference to this object.
     */
    public PreProcessorContainer processInclude(String text) {
        CharSequence cs = curIncludeType.process(text);
        preprocessedText.append(cs);
        return this;
    }

    /**
     * Returns the whole preprocessed text.
     * @return the whole preprocessed text. 
     */
    public String getPreprocessedText() {
        return preprocessedText.toString();
    }
    
    
    
}
