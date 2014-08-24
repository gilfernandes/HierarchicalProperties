/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model.node;

/**
 * Just a marker interface.
 * @author onepoint
 */
public interface SyntaxNode {

    /**
     * Sets the parent node.
     * @param node The parent node.
     */
    public void setParent(SyntaxNode node);

    /**
     * Returns the parent.
     * @return the parent node.
     */
    public SyntaxNode getParent();

    /**
     * Produces the output.
     * @return the string with the output of the component.
     */
    public CharSequence produce();

}
