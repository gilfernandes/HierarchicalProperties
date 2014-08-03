/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fernandes.properties.HierarchicalProperties;
import org.fernandes.properties.NodeProcessFunction;
import org.fernandes.properties.model.DefaultNode;
import org.fernandes.properties.model.Reference;
import org.fernandes.properties.util.Indentor;

/**
 * Contains the methods used by the parser to inject data into this object
 * and also the interface to access the underlying data tree with the 
 * properties.
 *
 * @author onepoint
 */
public class DefaultHierarchicalProperties implements HierarchicalProperties {
    
    /**
     * The logging class.
     */
    private static final Logger LOG = Logger.getLogger(DefaultHierarchicalProperties.class.getName());

    /**
     * The root node.
     */
    private final DefaultNode root = new DefaultNode(DefaultNode.ROOT_NODE_NAME, this);

    /**
     * The current pointer to
     */
    private DefaultNode curNode = root;

    /**
     * The current key.
     */
    private String curKey;
    
    /**
     * The current reference key.
     */
    private Reference curRefKey;
    
    /**
     * The current line comment.
     */
    private String curLineComment;
    
    /**
     * The reference list.
     */
    private final List<Reference> refList = new ArrayList<>();

    /**
     * An expression language map for different categories, like e.g the
     * enviroment and the Java system properties category.
     */
    private final Map<String, Map<String, String>> elVarMap = new HashMap<>();

    /**
     * Gets the value for a key from the current node.
     *
     * @param key The key from the current node.
     * @return the value for a key from the current node.
     */
    public String getCurrent(String key) {
        return curNode.getPropertyMap().get(key);
    }

    /**
     * Puts into the current node.
     *
     * @param key The key
     * @param value The value.
     * @return
     */
    public DefaultHierarchicalProperties put(String key, String value) {
        curNode.getPropertyMap().put(key, value);
        return this;
    }

    /**
     * The value to be set.
     *
     * @param key The value to be set.
     * @return this object
     */
    public DefaultHierarchicalProperties putKey(String key) {
        this.curKey = key;
        return this;
    }

    /**
     * The value to be set.
     *
     * @param value to be set.
     * @return this object
     */
    public DefaultHierarchicalProperties putValue(String value) {

        for (Map.Entry<String, Map<String, String>> entry : elVarMap.entrySet()) {
            for (Map.Entry<String, String> subEntry : entry.getValue().entrySet()) {
                String toReplace = String.format("\\$\\{%s\\.%s\\}", entry.getKey(), subEntry.getKey());
                value = value.replaceAll(toReplace, subEntry.getValue().replace("\\", "\\\\"));
            }
        }
        LOG.log(Level.INFO, "Key: {0} :: Value: {1}", new String[]{curKey, value});
        this.put(curKey, value);
        return this;
    }

    /**
     * Puts a current environment value.
     *
     * @param mapKey The key for the map used to lookup the values.
     * @param value The value of the current environment variable.
     * @return the value of the current environment variable.
     */
    public DefaultHierarchicalProperties putCurEnvVarMap(String mapKey, String value) {
        Map<String, String> varMap;
        if (elVarMap.containsKey(mapKey)) {
            varMap = this.elVarMap.get(mapKey);
        } else {
            varMap = new HashMap<>();
            this.elVarMap.put(mapKey, varMap);
        }
        switch (mapKey) {
            case "ENV":
                varMap.put(value, System.getenv(value));
                break;
            case "SYS":
                varMap.put(value, System.getProperty(value));
                break;
        }
        return this;
    }
    
    /**
     * Prepares a hierarchy node.
     *
     * @param hierarchyNode The node in the hierarchy.
     * @return the value of the current environment variable.
     */
    public DefaultHierarchicalProperties putReferenceNode(String hierarchyNode) {
        curRefKey = new Reference();
        curRefKey.setLocation(new DefaultNode(curNode));
        curRefKey.setSourceProperty(curKey);
        curRefKey.setTargetHierarchy(hierarchyNode);
        this.refList.add(curRefKey);
        return this;
    }
    
    /**
     * Puts a current environment value.
     *
     * @param referenceVal The targeted value.
     * @return the value of the current environment variable.
     */
    public DefaultHierarchicalProperties putReferenceValue(String referenceVal) {
        curRefKey.setTargetProperty(referenceVal);
        return this;
    }
    
    /**
     * Adds a multi-line comment to a node.
     *
     * @param multilineComment The multi-line comment to add to a node.
     * @return the value of the current environment variable.
     */
    public DefaultHierarchicalProperties addMultilineComment(String multilineComment) {
        curLineComment = multilineComment;
        
        curNode.addMultilineComment(curLineComment);
        return this;
    }
    
    /**
     * Adds a line comment to a node.
     *
     * @param lineComment The line comment to add to a node.
     * @return the value of the current environment variable.
     */
    public DefaultHierarchicalProperties addLineComment(String lineComment) {
        curNode.addLineComment(lineComment);
        return this;
    }
    
    

    /**
     * Creates a hierarchy with an expression like "/opt/test" where "/" is the
     * root node, "opt" the first child and "test" the grandchild of root. The
     * last found node is the node that will be set as the current node.
     *
     * @param nodeExpr The node expression.
     * @return a reference to this object.
     */
    public DefaultHierarchicalProperties createNodes(String nodeExpr) {
        String[] splits = nodeExpr.split("\\/");
        DefaultNode node = root;
        for (int i = 0, length = splits.length; i < length; i++) {
            String nodeName = splits[i].trim();
            if (node.getChildren().containsKey(nodeName)) {
                node = node.getChildren().get(nodeName);
            } else {
                DefaultNode child = new DefaultNode(nodeName, this);
                child.setParent(node);
                node.getChildren().put(nodeName, child);
                node = child;
            }
        }
        this.curNode = node;
        return this;
    }

    /**
     * Returns the string representation of this object.
     *
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        Stack<DefaultNode> stack = new Stack<>();
        stack.push(root);
        final String indent = "    ";
        final StringWriter stringWriter = new StringWriter();
        try (PrintWriter out = new PrintWriter(stringWriter, true)) {
            while (!stack.isEmpty()) {
                DefaultNode current = stack.pop();
                current.getChildren().entrySet().stream().map((entry) -> entry.getValue()).forEach((node) -> {
                    stack.push(node);
                });
                final int curDepth = current.getDepth();
                Indentor.printIndent(curDepth, out, indent);
                out.printf("- %s%n", current.getHierarchicalName());
                current.getPropertyMap().entrySet().stream().forEach((propEntry) -> {
                    Indentor.printIndent(curDepth, out, "    ");
                    out.printf("%s :: %s%n", propEntry.getKey(), propEntry.getValue());
                });
            }
            return stringWriter.toString();
        }
    }

    /**
     * Returns the iterator over all nodes.
     *
     * @return the iterator over all nodes.
     */
    @Override
    public Iterator<DefaultNode> iterator() {
        return new Iterator<DefaultNode>() {

            /**
             * The stack used for recursion.
             */
            private final Stack<DefaultNode> stack = new Stack<>();

            /**
             * Contains all nodes for iteration.
             */
            private final List<DefaultNode> allNodes = new ArrayList<>();

            /**
             * The current iterated node.
             */
            private DefaultNode curNode;

            /**
             * The iterator for all nodes.
             */
            private final Iterator<DefaultNode> allNodesIter;

            {
                stack.push(root);
                while (!stack.isEmpty()) {
                    final DefaultNode node = stack.pop();
                    allNodes.add(node);
                    node.getChildren().entrySet().stream().forEach((child) -> {
                        stack.push(child.getValue());
                    });
                }
                allNodesIter = allNodes.iterator();
            }

            @Override
            public boolean hasNext() {
                return allNodesIter.hasNext();
            }

            @Override
            public DefaultNode next() {
                return curNode = allNodesIter.next();
            }

            @Override
            public void remove() {
                allNodesIter.remove();
                DefaultNode parent = curNode.getParent();
                Map<String, DefaultNode> childrenMap = parent.getChildren();
                if (childrenMap != null) {
                    //childrenMap.remove(this)
                }
            }
        };
    }
    
    /**
     * Allows the implementation of a strategy for processing the nodes.
     * @param nodeProcessor The node processor.
     */
    @Override
    public void process(NodeProcessFunction nodeProcessor) {
        for (DefaultNode dn : this) {
            nodeProcessor.process(dn);
        }
    }
    
    /**
     * Returns the node count.
     * @return the node count. 
     */
    @Override
    public int nodeCount() {
        int[] count = new int[]{0};
        process((dn) -> count[0]++);
        return count[0];
    }
    
    /**
     * Returns a node by hierarchical name.
     * @param hierarchicalName The path like name of this node.
     * @return a node with hierarchicalName or {@code null}.
     */
    @Override
    public DefaultNode getNode(String hierarchicalName) {
        if(root.getHierarchicalName().equals(hierarchicalName)) {
            return root;
        }
        Stack<DefaultNode> nodeStack = new Stack<>();
        nodeStack.push(root);
        while(!nodeStack.isEmpty()) {
            DefaultNode presentNode = nodeStack.pop();
            Map<String, DefaultNode> map = presentNode.getChildren();
            for(Map.Entry<String, DefaultNode> entry : map.entrySet()) {
                DefaultNode child = entry.getValue();
                if(child.getHierarchicalName().equals(hierarchicalName)) {
                    return child;
                }
                nodeStack.push(child);
            }
        }
        return null;
    }
    
    /**
     * Returns the root node, parent of all other nodes.
     * @return the root node, parent of all other nodes.
     */
    @Override
    public DefaultNode getRoot(){
        return this.getNode("/");
    }
    
    /**
     * Looks up the references.
     */
    public void dereferenceRefs() {
        for(Reference ref : this.refList) {
            DefaultNode whereTochange = ref.getLocation();
            // the previous node needs to be reloaded.
            whereTochange = getNode(whereTochange.getHierarchicalName());
            String hierarchicalName = ref.getTargetHierarchy();
            DefaultNode targetNode = getNode(hierarchicalName);
            String targetProperty = ref.getTargetProperty();
            final String sourceProperty = ref.getSourceProperty();
            String targetVal = targetNode.getPropertyMap().get(targetProperty);
            String toChange = whereTochange.getPropertyMap().get(sourceProperty);
            if(toChange == null) {
                throw new RuntimeException("The property to change cannot be null.");
            }
            String changed = toChange.replaceAll("\\$\\s*\\{" + hierarchicalName + "\\s*\\:\\s*" + targetProperty + "}", targetVal);
            whereTochange.getPropertyMap().put(sourceProperty, changed);
        }
    }

}
