/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fernandes.properties.model.Node;
import org.fernandes.properties.model.Reference;
import org.fernandes.properties.util.Indentor;

/**
 * Contains hierarchical properties.
 *
 * @author onepoint
 */
public class HierarchicalProperties implements Iterable<Node> {
    
    /**
     * The logging class.
     */
    private static final Logger LOG = Logger.getLogger(HierarchicalProperties.class.getName());

    /**
     * The root node.
     */
    private final Node root = new Node(Node.ROOT_NODE_NAME, this);

    /**
     * The current pointer to
     */
    private Node curNode = root;

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
    public HierarchicalProperties put(String key, String value) {
        curNode.getPropertyMap().put(key, value);
        return this;
    }

    /**
     * Returns the entry set of the current node.
     *
     * @return the entry set of the current node.
     */
    public Set<Map.Entry<String, String>> entrySet() {
        return curNode.getPropertyMap().entrySet();
    }

    /**
     * The value to be set.
     *
     * @param key The value to be set.
     * @return this object
     */
    public HierarchicalProperties putKey(String key) {
        this.curKey = key;
        return this;
    }

    /**
     * The value to be set.
     *
     * @param value to be set.
     * @return this object
     */
    public HierarchicalProperties putValue(String value) {

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
    public HierarchicalProperties putCurEnvVarMap(String mapKey, String value) {
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
    public HierarchicalProperties putReferenceNode(String hierarchyNode) {
        curRefKey = new Reference();
        curRefKey.setLocation(new Node(curNode));
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
    public HierarchicalProperties putReferenceValue(String referenceVal) {
        curRefKey.setTargetProperty(referenceVal);
        return this;
    }
    
    /**
     * Adds a line comment to a node.
     *
     * @param lineComment The line comment to add to a node.
     * @return the value of the current environment variable.
     */
    public HierarchicalProperties addLineComment(String lineComment) {
        curLineComment = lineComment;
        curNode.addLineComment(curLineComment);
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
    public HierarchicalProperties createNodes(String nodeExpr) {
        String[] splits = nodeExpr.split("\\/");
        Node node = root;
        for (int i = 0, length = splits.length; i < length; i++) {
            String nodeName = splits[i].trim();
            if (node.getChildren().containsKey(nodeName)) {
                node = node.getChildren().get(nodeName);
            } else {
                Node child = new Node(nodeName, this);
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
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        final String indent = "    ";
        final StringWriter stringWriter = new StringWriter();
        try (PrintWriter out = new PrintWriter(stringWriter, true)) {
            while (!stack.isEmpty()) {
                Node current = stack.pop();
                for (Map.Entry<String, Node> entry : current.getChildren().entrySet()) {
                    final Node node = entry.getValue();
                    stack.push(node);
                }
                final int curDepth = current.getDepth();
                Indentor.printIndent(curDepth, out, indent);
                out.printf("- %s%n", current.getHierarchicalName());
                for (Map.Entry<String, String> propEntry : current.getPropertyMap().entrySet()) {
                    Indentor.printIndent(curDepth, out, "    ");
                    out.printf("%s :: %s%n", propEntry.getKey(), propEntry.getValue());
                }
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
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {

            /**
             * The stack used for recursion.
             */
            private final Stack<Node> stack = new Stack<>();

            /**
             * Contains all nodes for iteration.
             */
            private final List<Node> allNodes = new ArrayList<>();

            /**
             * The current iterated node.
             */
            private Node curNode;

            /**
             * The iterator for all nodes.
             */
            private final Iterator<Node> allNodesIter;

            {
                stack.push(root);
                while (!stack.isEmpty()) {
                    final Node node = stack.pop();
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
            public Node next() {
                return curNode = allNodesIter.next();
            }

            @Override
            public void remove() {
                allNodesIter.remove();
                Node parent = curNode.getParent();
                Map<String, Node> childrenMap = parent.getChildren();
                if (childrenMap != null) {
                    //childrenMap.remove(this)
                }
            }
        };
    }
    
    /**
     * Returns a node by hierarchical name.
     * @param hierarchicalName The path like name of this node.
     * @return a node with hierarchicalName or {@code null}.
     */
    public Node getNode(String hierarchicalName) {
        if(root.getHierarchicalName().equals(hierarchicalName)) {
            return root;
        }
        Stack<Node> nodeStack = new Stack<>();
        nodeStack.push(root);
        while(!nodeStack.isEmpty()) {
            Node presentNode = nodeStack.pop();
            Map<String, Node> map = presentNode.getChildren();
            for(Map.Entry<String, Node> entry : map.entrySet()) {
                Node child = entry.getValue();
                if(child.getHierarchicalName().equals(hierarchicalName)) {
                    return child;
                }
                nodeStack.push(child);
            }
        }
        return null;
    }
    
    /**
     * Looks up the references.
     */
    public void dereferenceRefs() {
        for(Reference ref : this.refList) {
            Node whereTochange = ref.getLocation();
            // the previous node needs to be reloaded.
            whereTochange = getNode(whereTochange.getHierarchicalName());
            String hierarchicalName = ref.getTargetHierarchy();
            Node targetNode = getNode(hierarchicalName);
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
