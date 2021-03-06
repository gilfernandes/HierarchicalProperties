/*
 OSSCUBE 2014
 */
package org.fernandes.properties.serialiser;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import org.fernandes.properties.model.HierarchicalProperties;
import org.fernandes.properties.model.DefaultNode;

/**
 * Default serialiser for the hierarchical properties.
 *
 * @author onepoint
 */
public class DefaultSerialiser implements HierarchichalPropertiesSerialiser {

    /**
     * Serialises the hierarchical properties.
     *
     * @param hProperties The hierarchi
     * @param writer
     */
    @Override
    public void serialize(HierarchicalProperties hProperties, Writer writer) {
        PrintWriter pWriter = new PrintWriter(writer);
        if (hProperties == null) {
            throw new IllegalArgumentException("Horizontal properties cannot be null.");
        }
        if (writer == null) {
            throw new IllegalArgumentException("Writer for hierarchical properties cannot be null.");
        }
        hProperties.process((DefaultNode dn) -> {
            String hierarchicalName = dn.getHierarchicalName();
            // Print hierarchical name
            if (!"/".equals(hierarchicalName)) {
                pWriter.printf("%n[%s]%n", hierarchicalName);
            }
            // Print the multi-line comments and the properties.
            HashMap<Integer, List<String>> multiComments = dn.getMultilineComments();
            HashMap<Integer, List<String>> lineComments = dn.getLineComments();
            final int[] idx = { 0 }; // like an integer pointer.
            dn.forEachPropertyMap((String key, String value) -> {
                writeComment(multiComments, idx, pWriter, "/* %s */%n");
                writeComment(lineComments, idx, pWriter, "# %s%n");
                pWriter.printf("%s = %s%n", key, value);
                idx[0]++;
            });
            // Write comments past the actual properties
            writeComment(multiComments, idx, pWriter, "/* %s */%n");
            writeComment(lineComments, idx, pWriter, "# %s%n");
        });
    }

    /**
     * Writes all the multi-line comments associated to a node.
     * @param multiComments The multi-line comments.
     * @param idx The pointer to the current position.
     * @param pWriter the writer to write to.
     */
    private void writeComment(HashMap<Integer, List<String>> multiComments, final int[] idx, PrintWriter pWriter, String pattern) {
        List<String> multiCommentList = multiComments.get(idx[0]);
        if(multiCommentList != null) {
            multiCommentList.forEach(s -> {
                pWriter.printf(pattern, s);
            });
        }
    }

}
