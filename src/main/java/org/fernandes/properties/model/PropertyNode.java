/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Interface with the property related methods.
 * @author onepoint
 */
public interface PropertyNode {
    
    /**
     * Returns a property value corresponding to {@code key} or
     * {@code null}, if the value was not found.
     * @param key The key for which to retrieve the property value.
     * @return a property value corresponding to {@code key} or
     * {@code null}, if the value was not found.
     */
    public String getProperty(String key);
    
    /**
     * Returns a property value corresponding to {@code key} or
     * {@code defaultVal}, if the value was not found.
     * @param key The key for which to retrieve the property value.
     * @param defaultVal The default value to be retrieved in case
     * the value for {@code key} was not found.
     * @return a property value corresponding to {@code key} or
     * {@code defaultVal}, if the value was not found.
     */
    public String getProperty(String key, String defaultVal);
    
    /**
     * Returns a property as integer.
     * @param key The key from which we are retrieving the integer.
     * @return a property as integer. Might return {@code null}.
     */
    public Integer getPropertyAsInt(String key);
    
    /**
     * Returns a property as integer.
     * @param key The key from which we are retrieving the integer.
     * @param defaultVal The default value, in case the property cannot be 
     * retrieved.
     * @return a property as integer.
     */
    public Integer getPropertyAsInt(String key, int defaultVal);
    
    /**
     * Returns a property as double.
     * @param key The key from which we are retrieving the integer.
     * @return a property as double. Might return {@code null}.
     */
    public Double getPropertyAsDouble(String key);
    
    /**
     * Returns a property as double.
     * @param key The key from which we are retrieving the integer.
     * @param defaultVal The default value, in case the property cannot be 
     * retrieved.
     * @return a property as double.
     */
    public Double getPropertyAsDouble(String key, double defaultVal);
    
    /**
     * Returns a property as a {@code boolean}.
     * @param key The key from which we are retrieving the integer.
     * @return a property as {@code boolean}. Might return {@code null}.
     */
    public Boolean getPropertyAsBoolean(String key);
    
    /**
     * Returns a property as {@code boolean}.
     * @param key The key from which we are retrieving the integer.
     * @param defaultVal The default value, in case the property cannot be 
     * retrieved.
     * @return a property as {@code boolean}.
     */
    public Boolean getPropertyAsBoolean(String key, boolean defaultVal);
    
    /**
     * Returns the map with all properties.
     * @return the map with all properties.
     */
    public Map<String, String> getPropertyMap();
    
    /**
     * Returns the name.
     * @return the name.
     */
    public String getName();

    /**
     * Exposes the forEach method for iterating through the properties.
     * @param action The action function.
     */
    void forEachPropertyMap(BiConsumer<? super String, ? super String> action);

    /**
     * Returns an iterator for the multi-line comments.
     * @return an iterator for the multi-line comments.
     */
    public Iterator<String> iteratorMultilineComment();
    
    /**
     * Returns an iterator for the multi-line comments.
     * @return an iterator for the multi-line comments.
     */
    public Iterator<String> iteratorLineComment();
    
    /**
     * Returns the children of the current node.
     * @return the children of the current node. 
     */
    public Map<String, DefaultNode> getChildren();
    
    /**
     * Returns the size of the multi-line comment.
     * @return the size of the multi-line comment.
     */
    public int sizeMultilineComment();
}
