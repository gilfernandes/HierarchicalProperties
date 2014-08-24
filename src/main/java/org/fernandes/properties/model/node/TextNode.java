/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model.node;

/**
 * A preprocessor node that allows to store text.
 * @author onepoint
 */
public class TextNode extends AbstractSyntaxNode {

    /**
     * The character sequence to store.
     */
    private final CharSequence cs;

    /**
     * The character sequence to save.
     * @param cs The character sequence to save.
     */
    public TextNode(CharSequence cs) {
        this.cs = cs;
    }

    /**
     * Returns the character sequence to save.
     * @return the character sequence to save.
     */
    public CharSequence getCs() {
        return cs;
    }

    /**
     * Returns the content of this node.
     * @return the content of this node.
     */
    @Override
    public CharSequence produce() {
        return cs;
    }


}
