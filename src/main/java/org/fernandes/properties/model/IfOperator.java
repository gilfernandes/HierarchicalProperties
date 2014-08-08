/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model;

/**
 * Represents the supported if operators.
 * @author onepoint
 */
public enum IfOperator {
    
    /**
     * Equality of value.
     */
    EQUALS("=="), 
    
    /**
     * Non equality of value.
     */
    NOT("!=");
    
    static {
        EQUALS.setReverse(NOT);
        NOT.setReverse(EQUALS);
    }
    
    /**
     * The sign of the operator.
     */
    String sign;
    
    /**
     * The reverse operator.
     */
    private IfOperator reverse;

    /**
     * Associates a sign to this entity.
     * @param sign The sign of the operator.
     */
    private IfOperator(String sign) {
        this.sign = sign;
    }
    
    /**
     * Returns the operator corresponding to a sign.
     * @param sign The sign to be converted into an if operator.
     * @return the operator corresponding to a sign.
     */
    public static IfOperator bySign(String sign) {
        for(IfOperator op : IfOperator.values()) {
            if(op.sign.equals(sign)) {
                return op;
            }
        }
        return null;
    }

    /**
     * Sets the reverse of this operator.
     * @param reverse The reverse of this operator.
     */
    public void setReverse(IfOperator reverse) {
        this.reverse = reverse;
    }

    /**
     * Returns the reverse of this operator.
     * @return the reverse of this operator. 
     */
    public IfOperator getReverse() {
        return reverse;
    }

    
}
