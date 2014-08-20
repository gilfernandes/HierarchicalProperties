/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/**
 * The node in the hierarchical properties.
 *
 * @author onepoint
 */
public class DefaultNode implements PropertyNode {

    /**
     * Pattern used to recognise integer values.
     */
    private static final Pattern INT_PAT = Pattern.compile("\\d+");

    /**
     * Pattern used to recognise double values.
     */
    private static final Pattern DOUBLE_PAT = Pattern.compile("\\d+(\\.\\d+)?");

    /**
     * Pattern used to recognise double values.
     */
    private static final Pattern BOOLEAN_PAT = Pattern.compile("(true|false)", Pattern.CASE_INSENSITIVE);

    /**
     * The root node name.
     */
    public static final String ROOT_NODE_NAME = "/";

    /**
     * The name of the node.
     */
    private final String name;

    /**
     * The parent node.
     */
    private DefaultNode parent;

    /**
     * The children nodes.
     */
    private final Map<String, DefaultNode> children = new LinkedHashMap<>();

    /**
     * The properties attached to this same node.
     */
    private final Map<String, String> propertyMap = new LinkedHashMap<>();

    /**
     * The parent hierarchical properties.
     */
    private final DefaultHierarchicalProperties outer;

    /**
     * The multi-line comments that have a position allowing the comments to be
     * associated with single properties.
     */
    private final HashMap<Integer, List<String>> multilineComments = new HashMap<>();

    /**
     * The line comments that have a position allowing the comments to be
     * associated with single properties. The key is the position.
     */
    private final HashMap<Integer, List<String>> lineComments = new HashMap<>();

    /**
     * Processes nodes in a specific way. Used to implement the strategy
     * pattern.
     *
     * @author onepoint
     */
    @FunctionalInterface
    private interface TypeProcessFunction<T> {

        /**
         * Interface used to process a node in a specific way.
         *
         * @param node The node to be processed.
         */
        public T process(String key);
    }

    /**
     * Copy constructor.
     *
     * @param original The node to copy from.
     */
    public DefaultNode(DefaultNode original) {
        this.name = original.name;
        if (original.parent != null) {
            this.parent = new DefaultNode(original.parent);
        }
        this.outer = original.outer;
        this.children.putAll(original.children);
        this.propertyMap.putAll(original.propertyMap);

    }

    /**
     * Associates this node to a name and hierarchical properties.
     *
     * @param name The name of the node.
     * @param outer The properties to which the node is associated.
     */
    public DefaultNode(String name, final DefaultHierarchicalProperties outer) {
        this.outer = outer;
        this.name = name;
    }

    /**
     * Gets the depth in the tree.
     *
     * @return the depth in the tree by navigating upwards.
     */
    public int getDepth() {
        int level = 0;
        DefaultNode cur = this;
        while (cur.parent != null) {
            level++;
            cur = cur.parent;
        }
        return level;
    }

    /**
     * Returns a hierarchical representation of the name in Unix style.
     *
     * @return a hierarchical representation of the name in Unix style.
     */
    public String getHierarchicalName() {
        if (ROOT_NODE_NAME.equals(name)) { // Root node special case.
            return ROOT_NODE_NAME;
        }
        StringBuilder builder = new StringBuilder();
        DefaultNode cur = this;
        while (cur.parent != null) {
            builder.insert(0, cur.name).insert(0, '/');
            cur = cur.parent;
        }
        return builder.toString().replaceAll("\\/$", "").replaceAll("^//", "/");
    }

    /**
     * Returns the string representation of this node.
     *
     * @return the string representation of this node.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        DefaultNode cur = this;
        builder.insert(0, cur.name + "/");
        while (cur.parent != null) {
            cur = cur.parent;
            builder.insert(0, cur.name + "/");
        }
        return builder.toString().replaceFirst("\\/$", "");
    }

    /**
     * Returns the parent node.
     *
     * @return the parent node.
     */
    public DefaultNode getParent() {
        return parent;
    }

    /**
     * Returns the children of the current node.
     *
     * @return the children of the current node.
     */
    @Override
    public Map<String, DefaultNode> getChildren() {
        return children;
    }

    /**
     * The size of the children associated to this node.
     *
     * @return the size of the children associated to this node.
     */
    public int sizeChildren() {
        return children.size();
    }

    /**
     * Returns the name.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns an iterator for the multi-line comments.
     *
     * @return an iterator for the multi-line comments.
     */
    @Override
    public Iterator<String> iteratorMultilineComment() {
        List<String> strList = deflateListOfLists(multilineComments);
        return strList.iterator();
    }

    /**
     * Deflates a list of lists.
     *
     * @param toFlattenList The lists to be flattened in on single list.
     * @return a flattened view of a list.
     */
    private List<String> deflateListOfLists(HashMap<Integer, List<String>> toFlattenList) {
        List<String> strList = new ArrayList<>(toFlattenList.size());
        toFlattenList.values().forEach(list -> {
            strList.addAll(list);
        });
        return strList;
    }

    /**
     * Returns an iterator for the multi-line comments.
     *
     * @return an iterator for the multi-line comments.
     */
    @Override
    public Iterator<String> iteratorLineComment() {
        List<String> strList = deflateListOfLists(lineComments);
        return strList.iterator();
    }

    /**
     * Returns the map with the multi-line comments and its positions.
     *
     * @return the map with the multi-line comments and its positions.
     */
    public HashMap<Integer, List<String>> getMultilineComments() {
        return multilineComments;
    }

    /**
     * Returns the list with the line comments.
     *
     * @return the list with the line comments.
     */
    public HashMap<Integer, List<String>> getLineComments() {
        return lineComments;
    }

    /**
     * Adds a multi-line comment to this node.
     *
     * @param e The line comment to add to this node.
     */
    public void addMultilineComment(String e) {
        addComment(multilineComments, propertyMap.size(), e);
    }

    /**
     * Adds a comment at a specific position.
     *
     * @param lineComments1 The comments multi-map.
     * @param pos The position for the key.
     * @param e The string to add to the list at a specific position.
     */
    private void addComment(final HashMap<Integer, List<String>> lineComments1, final int pos, String e) {
        if (lineComments1.containsKey(pos)) {
            lineComments1.get(pos).add(e);
        } else {
            List<String> mComments = new ArrayList<>();
            mComments.add(e);
            lineComments1.put(pos, mComments);
        }
    }

    /**
     * Adds a line comment to this node.
     *
     * @param e The line comment to add to this node.
     */
    public void addLineComment(String e) {
        addComment(lineComments, propertyMap.size(), e);
    }

    /**
     * Returns the size of the multi-line comment.
     *
     * @return the size of the multi-line comment.
     */
    @Override
    public int sizeMultilineComment() {
        return multilineComments.size();
    }

    /**
     * Returns the size of the line comments.
     *
     * @return the size of the line comments.
     */
    public int sizeLineComment() {
        return lineComments.size();
    }

    /**
     * Returns the property map.
     *
     * @return the property map.
     */
    @Override
    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    /**
     * Exposes the forEach method for iterating through the properties.
     *
     * @param action The action function.
     */
    @Override
    public void forEachPropertyMap(BiConsumer<? super String, ? super String> action) {
        propertyMap.forEach(action);
    }

    /**
     * Sets the parent node of this node.
     *
     * @param parent The parent of this node.
     */
    public void setParent(DefaultNode parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(String key) {
        return this.getPropertyMap().get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(String key, String defaultVal) {
        String val = this.getProperty(key);
        return val == null ? defaultVal : val;
    }

    /**
     * Returns a property as integer.
     *
     * @param key The key used to retrieve the integer.
     * @return the property value as integer or {@code null} in case the
     * property cannot be found or is not an integer.
     */
    @Override
    public Integer getPropertyAsInt(String key) {
        return extractType(key, val -> {
            return INT_PAT.matcher(val).matches() ? Integer.parseInt(val) : null;
        });
    }

    /**
     * Returns a property as integer.
     *
     * @param key The key from which we are retrieving the integer.
     * @param defaultVal The default value, in case the property cannot be
     * retrieved.
     * @return a property as integer.
     */
    @Override
    public Integer getPropertyAsInt(String key, int defaultVal) {
        Integer res = this.getPropertyAsInt(key);
        return res == null ? defaultVal : res;
    }

    /**
     * Returns a property as double.
     *
     * @param key The key from which we are retrieving the integer.
     * @return a property as double. Might return {@code null}.
     */
    @Override
    public Double getPropertyAsDouble(String key) {
        return extractType(key, val -> {
            return DOUBLE_PAT.matcher(val).matches() ? Double.parseDouble(val) : null;
        });
    }

    /**
     * Returns a property as double.
     *
     * @param key The key from which we are retrieving the integer.
     * @param defaultVal The default value, in case the property cannot be
     * retrieved.
     * @return a property as double.
     */
    @Override
    public Double getPropertyAsDouble(String key, double defaultVal) {
        Double res = this.getPropertyAsDouble(key);
        return res == null ? defaultVal : res;
    }

    /**
     * Returns a property as a {@code boolean}.
     *
     * @param key The key from which we are retrieving the integer.
     * @return a property as {@code boolean}. Might return {@code null}.
     */
    @Override
    public Boolean getPropertyAsBoolean(String key) {
        return extractType(key, val -> {
            return BOOLEAN_PAT.matcher(val).matches() ? Boolean.parseBoolean(val) : null;
        });
    }

    /**
     * Returns a property as {@code boolean}.
     *
     * @param key The key from which we are retrieving the integer.
     * @param defaultVal The default value, in case the property cannot be
     * retrieved.
     * @return a property as {@code boolean}.
     */
    @Override
    public Boolean getPropertyAsBoolean(String key, boolean defaultVal) {
        Boolean res = this.getPropertyAsBoolean(key);
        return res == null ? defaultVal : res;
    }

    /**
     * Returns a property as a {@code float}.
     * @param key The key from which we are retrieving the integer.
     * @return a property as {@code boolean}. Might return {@code null}.
     */
    @Override
    public Float getPropertyAsFloat(String key) {
        return extractType(key, val -> {
            return DOUBLE_PAT.matcher(val).matches() ? Float.parseFloat(val) : null;
        });
    }

    /**
     * Returns a property as {@code boolean}.
     * @param key The key from which we are retrieving the integer.
     * @param defaultVal The default value, in case the property cannot be
     * retrieved.
     * @return a property as {@code boolean}.
     */
    @Override
    public Float getPropertyAsFloat(String key, float defaultVal) {
        Float res = this.getPropertyAsFloat(key);
        return res == null ? defaultVal : res;
    }

    /**
     * Processes the extraction of a generic type, like e.g. Double, Integer,
     * etc.
     *
     * @param <T> The type being processed.
     * @param key The key used for extraction.
     * @param processFunction The function used to process the actual value from
     * a string to the actual type.
     * @return an extracted value with a specific type or if extraction fails
     * {@code null}.
     */
    private <T> T extractType(String key, TypeProcessFunction<T> processFunction) {
        String val = getProperty(key);
        if (val == null) {
            return null;
        }
        val = val.trim();
        return processFunction.process(val);
    }
}
