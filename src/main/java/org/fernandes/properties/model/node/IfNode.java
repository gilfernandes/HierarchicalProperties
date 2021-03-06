/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model.node;

import org.fernandes.properties.model.IfOperator;

/**
 * Contains the parts of a conditional expression.
 * @author onepoint
 */
public class IfNode extends ContainerNode {

    /**
     * The actual variable.
     */
    private String variable;

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
    public IfNode(String variable) {
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
     * Sets the variable. Used in the else if operation.
     * @param variable The internal variable name.
     */
    public void setVariable(String variable) {
        this.variable = variable;
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
     * @return The reverse of this operator.
     */
    public IfOperator reverseMatch() {
        return this.operator.getReverse();
    }

    /**
     * Returns the value of a variable.
     * @return the value of a variable.
     */
    public String getVariableValue() {
        return variableValue;
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

    /**
     * Returns the content of this node.
     * @return the content of this node.
     */
    @Override
    public CharSequence produce() {
        StringBuilder builder = new StringBuilder();
        if(isMatch()) {
            stream().forEach(node -> builder.append(node.produce()));
        }
        return builder;
    }




}
