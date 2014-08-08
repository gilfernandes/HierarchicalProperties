/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Container for the result of the pre processing of the hierarchical
 * properties.
 *
 * @author onepoint
 */
public class PreProcessorContainer {

    /**
     * The list with the includes.
     */
    private final StringBuilder preprocessedText = new StringBuilder();

    /**
     * The current include.
     */
    private IncludeType curIncludeType;

    /**
     * The map with pre-processor constants.
     */
    private final Map<String, String> constantMap = new LinkedHashMap<>();

    /**
     * The constant key.
     */
    private String constantKey;

    /**
     * The stack containing all the if expressions.
     */
    private final Stack<IfContainer> ifStack = new Stack<>();

    /**
     * Adds normal text with no processing to the includeList.
     *
     * @param text The text to be added to the include list.
     * @return a reference to this object.
     */
    public PreProcessorContainer processText(String text) {
        if (doProcess()) {
            preprocessedText.append(text);
        }
        return this;
    }

    /**
     * Sets the current include type.
     *
     * @param text The text representing n include type.
     * @return a reference to this object.
     */
    public PreProcessorContainer processCurIncludeType(String text) {
        if (doProcess()) {
            curIncludeType = IncludeType.byPrefix(text);
        }
        return this;
    }

    /**
     * Adds normal text with no processing to the includeList.
     *
     * @param text The text to be added to the include list.
     * @return a reference to this object.
     */
    public PreProcessorContainer processInclude(String text) {
        if (doProcess()) {
            CharSequence cs = curIncludeType.process(text);
            preprocessedText.append(cs);
        }
        return this;
    }

    public PreProcessorContainer addConstantKey(String key) {
        if (doProcess()) {
            this.constantKey = key;
        }
        return this;
    }

    /**
     * Adds a constant with its corresponding value.
     *
     * @param value The value in the constant map.
     * @return a reference to this object.
     */
    public PreProcessorContainer addConstantVal(String value) {
        if (doProcess()) {
            constantMap.put(constantKey, value);
        }
        return this;
    }

    /**
     * Adds a value of a define.
     *
     * @param key The key that should be in the constant map to be resolved. If
     * the key is not in the constant map it will be ignored.
     * @return a reference to this object.
     */
    public PreProcessorContainer addDefineVal(String key) {
        if (doProcess()) {
            if (constantMap.containsKey(key)) {
                String value = constantMap.get(key);
                preprocessedText.append(value);
            }
        }
        return this;
    }

    /**
     * Adds the if container with only the variable to the stack.
     *
     * @param variable The variable of the if statement.
     * @return a reference to this object.
     */
    public PreProcessorContainer ifStartVar(String variable) {
        IfContainer ifContainer = new IfContainer(variable);
        ifStack.push(ifContainer);
        return this;
    }

    /**
     * Adds the evaluation expression to the if on top of the stack. And
     * performs the evaluation.
     *
     * @param value The value of the if statement.
     * @return a reference to this object.
     */
    public PreProcessorContainer ifStartVal(String value) {
        IfContainer ifContainer = ifStack.peek();
        ifContainer.setValue(value);
        String variable = ifContainer.getVariable();
        String variableVal = constantMap.get(variable);
        if(variableVal == null) {
            throw new IllegalArgumentException(String.format("%s was not defined.", variable));
        }
        ifContainer.setVariableValue(variableVal);
        return this;
    }

    /**
     * Pops the top element from the stack.
     *
     * @return a reference to this object.
     */
    public PreProcessorContainer ifEnd() {
        ifStack.pop();
        return this;
    }
    
    /**
     * Pops the top element from the stack.
     *
     * @return a reference to this object.
     */
    public PreProcessorContainer ifElse() {
        IfContainer curIf = ifStack.peek();
        curIf.reverseMatch();
        return this;
    }
    
    /**
     * Associates the operator to the last if element on the stack.
     * @param operator The operator to associate to the last if element on the stack.
     * @return a reference to this object.
     */
    public PreProcessorContainer ifOperator(String operator) {
        IfContainer ifContainer = ifStack.peek();
        IfOperator op = IfOperator.bySign(operator);
        ifContainer.setOperator(op);
        return this;
    }

    /**
     * Returns {@code true} if the if stack is empty or if the if condition on
     * top of the if stack is {@code true}.
     *
     * @return {@code true} if the if stack is empty or if the if condition on
     * top of the if stack is {@code true}.
     */
    private boolean doProcess() {
        if (ifStack.isEmpty()) {
            return true;
        }
        boolean process = !ifStack.stream().anyMatch(i -> !i.isMatch());
        return process;
    }

    /**
     * Returns the whole preprocessed text.
     *
     * @return the whole preprocessed text.
     */
    public String getPreprocessedText() {
        return preprocessedText.toString();
    }

}
