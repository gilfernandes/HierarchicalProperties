/*
 OSSCUBE 2014
 */
package org.fernandes.properties.model.node;

/**
 * Contains the elements of a container.
 *
 * @author onepoint
 */
public class ForNode extends ContainerNode {

    /**
     * The start number.
     */
    private int start;

    /**
     * The end number.
     */
    private int end;

    /**
     * The step.
     */
    private int step;

    /**
     * The current value.
     */
    private int current;

    /**
     * The variable name.
     */
    private String varName;

    /**
     * Builds a node only with the variable name.
     *
     * @param varName The variable name.
     */
    public ForNode(String varName) {
        this.varName = varName;
        this.step = 1;
    }

    /**
     * Increments the for loop.
     *
     * @return the current counter or {@code 0}, if the for loop finished.
     */
    public int increment() {
        if (this.step > 0 && (this.current + this.step) > this.end) {
            return 0;
        } else if (this.step < 0 && (this.current - this.step) < this.end) {
            return 0;
        }
        this.current += step;
        return this.current;
    }

    /**
     * Sets the start value of the for.
     *
     * @param start The start value for the for.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Sets the end value of the for and calculates the step direction.
     *
     * @param end The end value of the for.
     */
    public void setEnd(int end) {
        this.end = end;
        if (start < end) {
            this.step = Math.abs(step);
        } else {
            this.step = this.step > 0 ? -this.step : this.step;
        }
    }

    /**
     * Returns the end value.
     *
     * @return the end value.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets the step.
     *
     * @param step The step to set.
     */
    public void setStep(int step) {
        this.step = step;
    }

    /**
     * Returns the string representation of this object.
     *
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        return "ForContainer{" + "start=" + start + ", end=" + end + ", step=" + step + ", current=" + current + '}';
    }

    /**
     * Calls the {@code produce} method of the children classes and aggregates its
     * output.
     *
     * @return the result of the aggregation of the child classes.
     */
    @Override
    public CharSequence produce() {
        StringBuilder builder = new StringBuilder();
        int i = start;
        if (step > 0) {
            for (; i <= end; i += step) {
                processChildren(builder, i);
            }
        } else if (step < 0) {
            for (; i >= end; i += step) {
                processChildren(builder, i);
            }
        }
        return builder;
    }

    private void processChildren(StringBuilder builder, int i) {
        stream().forEach(child -> {
            if(child instanceof VarNode) {
                VarNode node = (VarNode) child;
                node.put(varName, Integer.toString(i));
            }
            builder.append(child.produce());
        });
    }

}
