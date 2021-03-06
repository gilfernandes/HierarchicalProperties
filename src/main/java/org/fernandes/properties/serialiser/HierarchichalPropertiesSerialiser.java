/*
 OSSCUBE 2014
 */

package org.fernandes.properties.serialiser;

import java.io.Writer;
import org.fernandes.properties.model.HierarchicalProperties;

/**
 * Used to serialise Hierarchical properties.
 * @author onepoint
 */
public interface HierarchichalPropertiesSerialiser {
    
    /**
     * Serialises the hierarchical properties into a writer.
     * @param hProperties The properties to serialise.
     * @param writer The writer to write to.
     */
    public void serialize(HierarchicalProperties hProperties, Writer writer);
}
