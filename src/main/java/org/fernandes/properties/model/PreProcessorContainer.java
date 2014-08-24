/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import org.fernandes.properties.model.node.ContainerNode;
import org.fernandes.properties.model.node.ForNode;
import org.fernandes.properties.model.node.IfNode;
import org.fernandes.properties.model.node.IncludeNode;
import org.fernandes.properties.model.node.SyntaxNode;
import org.fernandes.properties.model.node.TextNode;
import org.fernandes.properties.model.node.VarNode;

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
    private final Stack<IfNode> ifStack = new Stack<>();

    /**
     * Used to contains syntax trees. Used to process the for loops and
     * everything below it.
     */
    private final ContainerNode parentContainer = new ContainerNode();

    /**
     * Adds normal text with no processing to the includeList.
     *
     * @param text The text to be added to the include list.
     * @return a reference to this object.
     */
    public PreProcessorContainer processText(String text) {
        if (doProcess()) {
            if (parentContainer.isEmpty()) {
                preprocessedText.append(text);
            } else {
                ForNode forNode = (ForNode) parentContainer.peekForNode();
                forNode.add(new TextNode(text));
            }
        }
        return this;
    }

    /**
     * Sets the current include type.
     *
     * @param text The text representing an include type.
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
            if (parentContainer.isEmpty()) {
                CharSequence cs = curIncludeType.process(text);
                preprocessedText.append(cs);
            } else {
                ForNode forNode = (ForNode) parentContainer.peekForNode();
                forNode.add(new IncludeNode(text, curIncludeType));
            }
        }
        return this;
    }

    /**
     * Adds a key for a constant.
     *
     * @param key The key for the constant.
     * @return a reference to this object.
     */
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
            if (parentContainer.isEmpty()) {
                constantMap.put(constantKey, value);
            }
            else {
                ForNode forNode = (ForNode) parentContainer.peekForNode();
                forNode.put(constantKey, value); // Thi variable exists only in the context of the "for".
            }
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
            if (parentContainer.isEmpty()) {
                if (key.startsWith(ExternalEnvironment.ENV.toString())) {
                    addEnvVal(key);
                } else if (key.startsWith(ExternalEnvironment.SYS.toString())) {
                    addSystemVal(key);
                } else if (constantMap.containsKey(key)) {
                    String value = constantMap.get(key);
                    if (value != null) {
                        preprocessedText.append(value.trim());
                    }
                }
            }
            else {
                ForNode forNode = (ForNode) parentContainer.peekForNode();
                forNode.putAll(constantMap);
                VarNode varNode = new VarNode(key);
                forNode.add(varNode);
            }
        }
        return this;
    }

    /**
     * Adds a value of a define from the OS environment.
     *
     * @param key The key that should be an environment variable. If the key is
     * not in the it will be ignored.
     * @return a reference to this object.
     */
    public PreProcessorContainer addEnvVal(String key) {
        if (doProcess()) {
            String realKey = key.replaceFirst("^" + ExternalEnvironment.ENV + ".", "");
            String value = System.getenv(realKey);
            if (value != null) {
                preprocessedText.append(value);
            }
        }
        return this;
    }

    /**
     * Adds a value of a define from the Java properties.
     *
     * @param key The key that should be a Java properties variable. If the key
     * is not in the it will be ignored.
     * @return a reference to this object.
     */
    public PreProcessorContainer addSystemVal(String key) {
        if (doProcess()) {
            String realKey = key.replaceFirst("^" + ExternalEnvironment.SYS + ".", "");
            String value = System.getProperty(realKey);
            if (value != null) {
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
        IfNode ifContainer = new IfNode(variable);
        ifStack.push(ifContainer);
        return this;
    }

    /**
     * Gets the current if container from the stack and replaces the variable in
     * it.
     *
     * @param variable The variable of the if statement.
     * @return a reference to this object.
     */
    public PreProcessorContainer elseIfStartVar(String variable) {
        IfNode ifContainer = ifStack.peek();
        ifContainer.setVariable(variable);
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
        IfNode ifContainer = ifStack.peek();
        ifContainer.setValue(value);
        String variable = ifContainer.getVariable();
        String variableVal = constantMap.get(variable);
        if (variableVal == null) {
            throw new IllegalArgumentException(String.format("%s was not defined.", variable));
        }
        ifContainer.setVariableValue(variableVal);
        return this;
    }

    /**
     * Adds the variable of a "for".
     *
     * @param value The value of the variable in the for statement.
     * @return a reference to this object.
     */
    public PreProcessorContainer forVar(String value) {
        // Extract from "i = 1 : 10" or "for i = 1 : 2 : 10"
        ForNode forNode = new ForNode(value);
        parentContainer.add(forNode);
        return this;
    }

    /**
     * Adds the start value of a "for".
     *
     * @param value The start value of the variable in the for statement.
     * @return a reference to this object.
     */
    public PreProcessorContainer forStart(String value) {
        // Extract from "i = 1 : 10" or "for i = 1 : 2 : 10"
        ForNode forNode = (ForNode) parentContainer.peekForNode();
        forNode.setStart(Integer.parseInt(value));
        return this;
    }

    /**
     * Adds the end value of a "for". Note: this value may be considered the
     * step if another ": \d+" comes after this value here.
     *
     * @param value The end or step value of the variable in the for statement.
     * @return a reference to this object.
     */
    public PreProcessorContainer forEndOrStep(String value) {
        // Extract from "i = 1 : 10" or "for i = 1 : 2 : 10"
        ForNode forNode = (ForNode) parentContainer.peekForNode();
        forNode.setEnd(Integer.parseInt(value));
        return this;
    }

    /**
     * Adds the end value of a "for". Note: this value may be considered the
     * step if another ": \d+" comes after this value here.
     *
     * @return a reference to this object.
     */
    public PreProcessorContainer forEnd() {
        // Extract from "i = 1 : 10" or "for i = 1 : 2 : 10"
        ForNode forNode = (ForNode) parentContainer.peekForNode();
        SyntaxNode parent = forNode.getParent();
        if (parent == parentContainer) {
            preprocessedText.append(parentContainer.produce());
            parentContainer.clear(); // clear up.
        }
        return this;
    }

    /**
     * Adds the end value of a "for".
     *
     * @param value The end value of the variable in the for statement.
     * @return a reference to this object.
     */
    public PreProcessorContainer forEnd(String value) {
        // Extract from "i = 1 : 10" or "for i = 1 : 2 : 10"
        ForNode forNode = (ForNode) parentContainer.peekForNode();
        int step = forNode.getEnd();
        forNode.setStep(step);
        forNode.setEnd(Integer.parseInt(value));
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
        IfNode curIf = ifStack.peek();
        IfOperator reverse = curIf.reverseMatch();
        curIf.setOperator(reverse);
        curIf.setVariableValue(curIf.getVariableValue()); // re-evaluation of the match
        return this;
    }

    /**
     * Associates the operator to the last if element on the stack.
     *
     * @param operator The operator to associate to the last if element on the
     * stack.
     * @return a reference to this object.
     */
    public PreProcessorContainer ifOperator(String operator) {
        IfNode ifContainer = ifStack.peek();
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
