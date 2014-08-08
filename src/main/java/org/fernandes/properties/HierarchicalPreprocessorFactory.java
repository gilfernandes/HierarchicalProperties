/*
 OSSCUBE 2014
 */

package org.fernandes.properties;

import java.nio.file.Path;

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
        return createInstance(path, false);
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
        String included = PreProcessorFactory.createInstance(path);
        HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(included, true);
        if(autoReload) {
            Reloader.INSTANCE.startReloadThread(path, props);
        }
        return props;
    }
}
