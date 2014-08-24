/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model.node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A container of syntax nodes.
 * @author onepoint
 */
public class ContainerNode extends AbstractSyntaxNode {

    /**
     * The list of children
     */
    private final List<SyntaxNode> children = new ArrayList<>();

    /**
     * The map with pre-processor constants.
     */
    private final Map<String, String> constantMap = new LinkedHashMap<>();

    /**
     * Used to build the produced result of all components.
     */
    private final StringBuilder builder = new StringBuilder();

    /**
     * Adds a preprocessor node.
     * @param e The node to add.
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(SyntaxNode e) {
        e.setParent(this);
        return children.add(e);
    }

    /**
     * Removes a preprocessor node.
     * @param o The pre-processor node.
     * @return <tt>true</tt> if this list contained the specified element.
     */
    public boolean remove(SyntaxNode o) {
        return children.remove(o);
    }

    /**
     * Returns the element on top of the stack.
     * @return The last element.
     */
    public ForNode peekForNode() {
        if(children.isEmpty()) {
            return null;
        }
        int forNodePos = findForNodePos();
        switch(forNodePos) {
            case -1:
                return null;
            default:
                return (ForNode) children.get(forNodePos);
        }
    }

    /**
     * Finds the position of the last for node.
     * @return the position of the last for node.
     */
    private int findForNodePos() {
        int forNodePos = -1;
        for(int i = children.size() - 1; i >= 0; i--) {
            final SyntaxNode child = children.get(i);
            if(child instanceof ForNode) {
                forNodePos = i;
                break;
            }
        }
        return forNodePos;
    }

    /**
     * Removes the last element.
     * @return a for node in case it is found.
     */
    public ForNode popForNode() {
        if(!children.isEmpty()) {
            int forNodePos = findForNodePos();
            return (ForNode) children.remove(forNodePos);
        }
        else {
            return null;
        }
    }

    /**
     * Clears the children.
     */
    public void clear() {
        children.clear();
    }

    /**
     * Returns the stream to process the children.
     * @return the stream to process the children.
     */
    public Stream<SyntaxNode> stream() {
        return children.stream();
    }

    /**
     * Checks, if there are no children.
     * @return return {@code true} if there are no children, else {@code false}.
     */
    public boolean isEmpty() {
        return children.isEmpty();
    }

    /**
     * Returns an empty string.
     * @return an empty string.
     */
    @Override
    public CharSequence produce() {
        stream().forEach(node -> {
            builder.append(node.produce());
        });
        return builder;
    }

    /**
     * The constants in this container.
     * @param key The key to be retrieved.
     * @return the value corresponding to {@code key} or {@code null}.
     */
    public String get(String key) {
        return constantMap.get(key);
    }

    /**
     * Inserts a key value pair into the constant map.
     * @param key The key to be inserted into the map.
     * @param value The value to insert.
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>,
     *         if the implementation supports <tt>null</tt> values.)
     */
    public String put(String key, String value) {
        return constantMap.put(key, value);
    }

    /**
     * Used to inject the full context into this tree node.
     * @param m The map to inject into this one.
     */
    public void putAll(Map<? extends String, ? extends String> m) {
        constantMap.putAll(m);
    }



    /**
     * Returns {@code true}, if a key is in the {@code constantMap}, else {@cdoe false}.
     * @param key The key to be checked.
     * @return {@code true}, if a key is in the {@code constantMap}, else {@cdoe false}.
     */
    public boolean containsKey(String key) {
        return constantMap.containsKey(key);
    }





}