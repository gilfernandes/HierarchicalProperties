/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model.node;

/**
 * Abstract syntax node, base class for most syntax node implementations.
 *
 * @author onepoint
 */
public abstract class AbstractSyntaxNode implements SyntaxNode {

    /**
     * The parent node.
     */
    private SyntaxNode parent;

    /**
     * Sets the parent node.
     * @param node The parent node.
     */
    @Override
    public void setParent(SyntaxNode node) {
        this.parent = node;
    }

    /**
     * Retrieves the parent node.
     * @return the parent node.
     */
    @Override
    public SyntaxNode getParent() {
        return this.parent;
    }

}
