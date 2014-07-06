/*
 OSSCUBE 2014
 */
package org.fernandes.properties.util;

import java.io.PrintWriter;

/**
 *
 * @author onepoint
 */
public class Indentor {

    /**
     * Prints the indent.
     *
     * @param indentLevel The indentation level.
     * @param out The writer to which to write.
     */
    public static void printIndent(final Integer indentLevel, PrintWriter out, String indentExpr) {
        for (int i = 0, level = indentLevel; i < level; i++) {
            out.print(indentExpr);
        }
    }

}
