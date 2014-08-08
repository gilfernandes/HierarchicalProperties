/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model;

/**
 * Contains the parts of a conditional expression.
 * @author onepoint
 */
public class IfContainer {
    
    /**
     * The actual variable.
     */
    private final String variable;
    
    /**
     * The actual value.
     */
    private String value;
    
    /**
     * The actual variable value.
     */
    private String variableValue;
    
    /**
     * If this expression is {@code true} or {@code false}.
     */
    private boolean match;
    
    /**
     * The operator being used here.
     */
    private IfOperator operator;

    /**
     * Associates the variable directly to this object.
     * @param variable The variable.
     */
    public IfContainer(String variable) {
        this.variable = variable;
    }

    /**
     * Sets the declared value.
     * @param value The declared value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the variable value.
     * @param variableValue The variable value to be set.
     */
    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
        switch(operator) {
            case EQUALS:
                match = value.matches(variableValue);
                break;
            case NOT:
                match = !value.matches(variableValue);
                break;
        }
    }

    /**
     * Returns {@code true} if {@code value} and {@code variableValue} match, else {@code false}.
     * @return {@code true} if {@code value} and {@code variableValue} match, else {@code false}. 
     */
    public boolean isMatch() {
        return match;
    }

    /**
     * Gets the variable name to be evaluated.
     * @return the variable name to be evaluated.
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the operator that determines, how the match is to be executed.
     * @param operator The operator that determines how to match is to be executed.
     */
    public void setOperator(IfOperator operator) {
        this.operator = operator;
    }
    
    /**
     * Just reverses the operator. {@code false} becomes {@code true} and
     * {@code true} becomes {@code false}.
     */
    public void reverseMatch() {
        this.operator = this.operator.getReverse();
    }

    /**
     * Returns the string representation of this object.
     * @return the string representation of this object. 
     */
    @Override
    public String toString() {
        return "IfContainer{" + "variable=" + variable + ", value=" + value + ", variableValue=" + variableValue + 
                ", match=" + match + ", operator=" + operator + '}';
    }
    
    
    
    
    
    
}
