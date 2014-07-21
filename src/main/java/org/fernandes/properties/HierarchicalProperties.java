/*
 OSSCUBE 2014
 */

package org.fernandes.properties;

import org.fernandes.properties.model.DefaultNode;
import org.fernandes.properties.model.PropertyNode;

/**
 * Contains the public methods of all hierarchical properties.
 * @author onepoint
 */
public interface HierarchicalProperties extends Iterable<DefaultNode> {
    
    /**
     * Returns a node by hierarchical name.
     * @param hierarchicalName The path like name of this node.
     * @return a node with hierarchicalName or {@code null}.
     */
    public PropertyNode getNode(String hierarchicalName);
    
    /**
     * Returns the root node, parent of all other nodes.
     * @return the root node, parent of all other nodes.
     */
    public PropertyNode getRoot();
    
    /**
     * Allows the implementation of a strategy for processing the nodes.
     * @param nodeProcessor The node processor.
     */
    public void process(NodeProcessFunction nodeProcessor);
}
