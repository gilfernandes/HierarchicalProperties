/*
 OSSCUBE 2014
 */

package org.fernandes.properties;

import org.fernandes.properties.model.DefaultNode;

/**
 * Processes nodes in a specific way. Used 
 * to implement the strategy pattern.
 * @author onepoint
 */
@FunctionalInterface
public interface NodeProcessFunction {
    
    /**
     * Interface used to process a node in a specific way.
     * @param node The node to be processed.
     */
    public void process(DefaultNode node);
}
