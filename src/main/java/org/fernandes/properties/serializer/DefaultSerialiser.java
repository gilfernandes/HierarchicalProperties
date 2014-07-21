/*
 OSSCUBE 2014
 */
package org.fernandes.properties.serializer;

import java.io.PrintWriter;
import java.io.Writer;
import org.fernandes.properties.HierarchicalProperties;
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
            if (!"/".equals(hierarchicalName)) {
                pWriter.printf("[%s]%n", hierarchicalName);
                dn.forEachPropertyMap((String key, String value) -> {
                    pWriter.printf("%s = %s%n", key, value);
                });
            }
        });
    }

}
