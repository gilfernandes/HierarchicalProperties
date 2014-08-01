/*
 OSSCUBE 2014
 */

package org.fernandes.properties;

import java.util.LinkedHashMap;
import java.util.Map;
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
     * The map with pre-processor constants.
     */
    private Map<String, String> constantMap = new LinkedHashMap<>();
    
    /**
     * The constant key.
     */
    private String constantKey;
    
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
    
    public PreProcessorContainer addConstantKey(String key) {
        this.constantKey = key;
        return this;
    }
    
    /**
     * Adds a constant with its corresponding value.
     * @param value The value in the constant map.
     * @return a reference to this object.
     */
    public PreProcessorContainer addConstantVal(String value) {
        constantMap.put(constantKey, value);
        return this;
    }
    
    /**
     * Adds a value of a define.
     * @param key The key that should be in the constant map to be resolved.
     * If the key is not in the constant map it will be ignored.
     * @return a reference to this object.
     */
    public PreProcessorContainer addDefineVal(String key) {
        if(constantMap.containsKey(key)) {
            String value = constantMap.get(key);
            preprocessedText.append(value);
        }
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
