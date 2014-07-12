/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model;

/**
 * A reference in the hierarchical properties.
 * @author onepoint
 */
public class Reference {
   
    /**
     * The node where it is found.
     */
    private DefaultNode location;
    
    /**
     * The source property.
     */
    private String sourceProperty;
    
    /**
     * The target hierarchy.
     */
    private String targetHierarchy;
    
    /**
     * The target property.
     */
    private String targetProperty;

    /**
     * Empty constructor.
     */
    public Reference() {
    }
    
    

    /**
     * Associates this object to a location node with the target hierarchy and 
     * the target property.
     * @param location The current location in the tree.
     * @param targetHierarchy The target hierarchy.
     * @param targetProperty The target property.
     */
    public Reference(DefaultNode location, String targetHierarchy, String targetProperty) {
        this.location = location;
        this.targetHierarchy = targetHierarchy;
        this.targetProperty = targetProperty;
    }

    /**
     * Returns the location in the tree.
     * @return the location in the tree.
     */
    public DefaultNode getLocation() {
        return location;
    }

    /**
     * Returns the target hierarchy.
     * @return the target hierarchy.
     */
    public String getTargetHierarchy() {
        return targetHierarchy;
    }

    /**
     * Returns the target property.
     * @return the target property.
     */
    public String getTargetProperty() {
        return targetProperty;
    }

    /**
     * Returns the string representation of this object.
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        return "Reference{" + "location=" + location + ", targetHierarchy=" + targetHierarchy + ", targetProperty=" + targetProperty + '}';
    }

    /**
     * Sets the location in the tree.
     * @param location The location in the tree.
     */
    public void setLocation(DefaultNode location) {
        this.location = location;
    }

    /**
     * Sets the target hierarchy.
     * @param targetHierarchy The target hierarchy.
     */
    public void setTargetHierarchy(String targetHierarchy) {
        this.targetHierarchy = targetHierarchy;
    }

    /**
     * Sets the target property.
     * @param targetProperty The target property.
     */
    public void setTargetProperty(String targetProperty) {
        this.targetProperty = targetProperty;
    }

    /**
     * The source property where to dereference.
     * @return the source property where to dereference.
     */
    public String getSourceProperty() {
        return sourceProperty;
    }

    /**
     * Sets the source property where to dereference.
     * @param sourceProperty The source property where to dereference.
     */
    public void setSourceProperty(String sourceProperty) {
        this.sourceProperty = sourceProperty;
    }
    
    
    
    
}
