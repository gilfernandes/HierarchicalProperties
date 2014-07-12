/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model;

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
}
