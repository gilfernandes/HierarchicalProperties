/*
 OSSCUBE 2014
 */

package org.fernandes.properties.factory;

import java.nio.file.Path;
import org.fernandes.properties.model.HierarchicalProperties;
import org.fernandes.properties.Reloader;

/**
 * Preprocesses and then instantiates the hierarchical properties.
 * 
 * @author onepoint
 */
public class HierarchicalPreprocessorFactory {
    
    /**
     * Creates an instance of hierarchical properties after preprocessing 
     * the files.
     * @param path The path to be preprocessed and then to be converted 
     * to hierarchical properties.
     * @return an instance of hierarchical properties.
     */
    public static HierarchicalProperties createInstance(Path path) {
        if(path == null) {
            return null;
        }
        return createInstance(path, false);
    }
    
    /**
     * Creates an instance of hierarchical properties after preprocessing 
     * the files.
     * @param cp The classpath from which to include.
     * @return an instance of hierarchical properties.
     */
    public static HierarchicalProperties createInstanceCp(String cp) {
        if(cp == null) {
            return null;
        }
        String preprocessed = PreProcessorFactory.createInstanceFromCp(cp);
        HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(preprocessed, true);
        return props;
    }
    
    /**
     * Creates an instance of hierarchical properties after preprocessing 
     * the files.
     * @param path The path to be preprocessed and then to be converted 
     * to hierarchical properties.
     * @param autoReload If {@code true} the hierarchical properties are reloaded
     * when the file is changed.
     * @return an instance of hierarchical properties.
     */
    public static HierarchicalProperties createInstance(Path path, boolean autoReload) {
        if(path == null) {
            return null;
        }
        String included = PreProcessorFactory.createInstance(path);
        HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
        if(autoReload) {
            Reloader.INSTANCE.startReloadThread(path, props);
        }
        return props;
    }
}
