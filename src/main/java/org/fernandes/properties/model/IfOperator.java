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
    
    /**
     * The sign of the operator.
     */
    String sign;

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
    
    
}
