/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.fernandes.properties.HierarchicalProperties;

/**
 * The node in the hierarchical properties.
 *
 * @author onepoint
 */
public class Node {
    
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
    private Node parent;
    
    /**
     * The children nodes.
     */
    private final Map<String, Node> children = new LinkedHashMap<>();
    
    /**
     * The properties attached to this same node.
     */
    private final Map<String, String> propertyMap = new LinkedHashMap<>();
    
    /**
     * The parent hierarchical properties.
     */
    private final HierarchicalProperties outer;
    
    /**
     * The line comments.
     */
    private List<String> lineComments;
    
    /**
     * Copy constructor.
     * @param original The node to copy from.
     */
    public Node(Node original) {
        this.lineComments = new ArrayList<>();
        this.name = original.name;
        if(original.parent != null) {
            this.parent = new Node(original.parent);
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
    public Node(String name, final HierarchicalProperties outer) {
        this.lineComments = new ArrayList<>();
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
        Node cur = this;
        while (cur.parent != null) {
            level++;
            cur = cur.parent;
        }
        return level;
    }

    /**
     * Returns all the names
     *
     * @return
     */
    public String getHierarchicalName() {
        if(ROOT_NODE_NAME.equals(name)) { // Root node special case.
            return ROOT_NODE_NAME;
        }
        StringBuilder builder = new StringBuilder();
        Node cur = this;
        while (cur.parent != null) {
            builder.insert(0, cur.name).insert(0, '/');
            cur = cur.parent;
        }
        return builder.toString().replaceAll("\\/$", "");
    }
    
    /**
     * Returns the string representation of this node.
     * @return the string representation of this node.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node cur = this;
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
    public Node getParent() {
        return parent;
    }

    /**
     * Returns the children of the current node.
     * @return the children of the current node. 
     */
    public Map<String, Node> getChildren() {
        return children;
    }

    /**
     * Returns the name.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an iterator for the line comments.
     * @return an iterator for the line comments.
     */
    public Iterator<String> iteratorLineComment() {
        return lineComments.iterator();
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
     * Returns the size of the line comment.
     * @return the size of the line comment.
     */
    public int sizeLineComment() {
        return lineComments.size();
    }

    /**
     * Returns the property map.
     * @return the property map. 
     */
    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    /**
     * Sets the parent node of this node.
     * @param parent The parent of this node.
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    
    
    

}
