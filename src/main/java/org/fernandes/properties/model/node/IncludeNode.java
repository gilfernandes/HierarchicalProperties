/*
 OSSCUBE 2014
 */

package org.fernandes.properties.model.node;

import org.fernandes.properties.model.IncludeType;

/**
 * Container for includes.
 * @author onepoint
 */
public class IncludeNode extends AbstractSyntaxNode {

    /**
     * The current include.
     */
    private final IncludeType curIncludeType;

    /**
     * The include's container target.
     */
    private final String target;

    /**
     * Associates a target with this container.
     * @param target The target to be associated with this container.
     * @param curIncludeType The current type of include.
     */
    public IncludeNode(String target, IncludeType curIncludeType) {
        this.target = target;
        this.curIncludeType = curIncludeType;
    }

    /**
     * Returns the target to be associated with this container.
     * @return the target to be associated with this container.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Returns an empty string.
     * @return an empty string.
     */
    @Override
    public CharSequence produce() {
        if(curIncludeType == null || target == null) {
            return "";
        }
        return curIncludeType.process(target);
    }
}
