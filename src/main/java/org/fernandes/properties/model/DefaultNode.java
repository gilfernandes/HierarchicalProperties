/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.fernandes.properties.DefaultHierarchicalProperties;

/**
 * The node in the hierarchical properties.
 *
 * @author onepoint
 */
public class DefaultNode implements PropertyNode {
    
    /**
     * The root node name.
     */
    public static final String ROOT_NODE_NAME = "/";

    /**
     * The name of the node.
     */
    private final String name;
    
    /**
     * The parent node.
     */
    private DefaultNode parent;
    
    /**
     * The children nodes.
     */
    private final Map<String, DefaultNode> children = new LinkedHashMap<>();
    
    /**
     * The properties attached to this same node.
     */
    private final Map<String, String> propertyMap = new LinkedHashMap<>();
    
    /**
     * The parent hierarchical properties.
     */
    private final DefaultHierarchicalProperties outer;
    
    /**
     * The multi-line comments.
     */
    private final List<String> multilineComments = new ArrayList<>();
    
    /**
     * The line comments.
     */
    private final List<String> lineComments = new ArrayList<>();
    
    /**
     * Copy constructor.
     * @param original The node to copy from.
     */
    public DefaultNode(DefaultNode original) {
        this.name = original.name;
        if(original.parent != null) {
            this.parent = new DefaultNode(original.parent);
        }
        this.outer = original.outer;
        this.children.putAll(original.children);
        this.propertyMap.putAll(original.propertyMap);
        
    }
    
    /**
     * Associates this node to a name and hierarchical properties.
     * @param name The name of the node.
     * @param outer The properties to which the node is associated.
     */
    public DefaultNode(String name, final DefaultHierarchicalProperties outer) {
        this.outer = outer;
        this.name = name;
    }

    /**
     * Gets the depth in the tree.
     *
     * @return the depth in the tree by navigating upwards.
     */
    public int getDepth() {
        int level = 0;
        DefaultNode cur = this;
        while (cur.parent != null) {
            level++;
            cur = cur.parent;
        }
        return level;
    }

    /**
     * Returns a hierarchical representation of the name in Unix style.
     *
     * @return a hierarchical representation of the name in Unix style.
     */
    public String getHierarchicalName() {
        if(ROOT_NODE_NAME.equals(name)) { // Root node special case.
            return ROOT_NODE_NAME;
        }
        StringBuilder builder = new StringBuilder();
        DefaultNode cur = this;
        while (cur.parent != null) {
            builder.insert(0, cur.name).insert(0, '/');
            cur = cur.parent;
        }
        return builder.toString().replaceAll("\\/$", "").replaceAll("^//", "/");
    }
    
    /**
     * Returns the string representation of this node.
     * @return the string representation of this node.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        DefaultNode cur = this;
        builder.insert(0, cur.name + "/");
        while(cur.parent != null) {
            cur = cur.parent;
            builder.insert(0, cur.name + "/");
        }
        return builder.toString().replaceFirst("\\/$", "");
    }

    /**
     * Returns the parent node.
     * @return the parent node. 
     */
    public DefaultNode getParent() {
        return parent;
    }

    /**
     * Returns the children of the current node.
     * @return the children of the current node. 
     */
    @Override
    public Map<String, DefaultNode> getChildren() {
        return children;
    }

    /**
     * Returns the name.
     * @return the name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns an iterator for the multi-line comments.
     * @return an iterator for the multi-line comments.
     */
    @Override
    public Iterator<String> iteratorMultilineComment() {
        return multilineComments.iterator();
    }
    
    /**
     * Returns an iterator for the multi-line comments.
     * @return an iterator for the multi-line comments.
     */
    @Override
    public Iterator<String> iteratorLineComment() {
        return lineComments.iterator();
    }

    /**
     * Returns the list with the multi-line comments.
     * @return the list with the multi-line comments.
     */
    public List<String> getMultilineComments() {
        return multilineComments;
    }

    /**
     * Returns the list with the line comments.
     * @return the list with the line comments.
     */
    public List<String> getLineComments() {
        return lineComments;
    }
    
    

    /**
     * Adds a multi-line comment to this node.
     * @param e The line comment to add to this node.
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean addMultilineComment(String e) {
        return multilineComments.add(e);
    }
    
    /**
     * Adds a line comment to this node.
     * @param e The line comment to add to this node.
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean addLineComment(String e) {
        return lineComments.add(e);
    }

    /**
     * Returns the size of the multi-line comment.
     * @return the size of the multi-line comment.
     */
    @Override
    public int sizeMultilineComment() {
        return multilineComments.size();
    }
    
    /**
     * Returns the size of the line comments.
     * @return the size of the line comments. 
     */
    public int sizeLineComment() {
        return lineComments.size();
    }

    /**
     * Returns the property map.
     * @return the property map. 
     */
    @Override
    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    /**
     * Exposes the forEach method for iterating through the properties.
     * @param action The action function.
     */
    @Override
    public void forEachPropertyMap(BiConsumer<? super String, ? super String> action) {
        propertyMap.forEach(action);
    }
    
    /**
     * Sets the parent node of this node.
     * @param parent The parent of this node.
     */
    public void setParent(DefaultNode parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(String key) {
        return this.getPropertyMap().get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(String key, String defaultVal) {
        String val = this.getProperty(key);
        return val == null ? defaultVal : val;
    }
}
