/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model.node;

/**
 * Node containing a variable.
 * @author onepoint
 */
public class VarNode extends AbstractSyntaxNode {

    /**
     * The whole text of the variable.
     */
    private String var;

    /**
     * Associates this value with the variable name and the constant map.
     * @param var The variable name.
     */
    public VarNode(String var) {
        this.var = var;
    }

    /**
     * Returns the variable.
     * @return the variable.
     */
    public String getVar() {
        return var;
    }

    /**
     * Sets the variable.
     * @param var The variable.
     */
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * Puts a value into the constant map.
     * @param key The name of the constant.
     * @param value The value of the constant.
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>,
     *         if the implementation supports <tt>null</tt> values.)
     */
    public String put(String key, String value) {
        if(this.getParent() instanceof ContainerNode) {
            ContainerNode parentNode = (ContainerNode) this.getParent();
            return parentNode.put(key, value);
        }
        return "";
    }

    /**
     * Returns the value of the
     * @return an empty string.
     */
    @Override
    public CharSequence produce() {
        if(this.getParent() instanceof ContainerNode) {
            ContainerNode parentNode = (ContainerNode) this.getParent();
            return parentNode.containsKey(var) ? parentNode.get(var) : "";
        }
        return "";
    }
}
