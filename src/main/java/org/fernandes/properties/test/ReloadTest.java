/*
 OSSCUBE 2014
 */

package org.fernandes.properties.test;

import java.beans.PropertyChangeEvent;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fernandes.properties.HierarchicalProperties;
import org.fernandes.properties.HierarchicalPropertiesFactory;

/**
 * 
 * @author onepoint
 */
public class ReloadTest {
    
    public static void main(String[] args) {
        try {
            HierarchicalProperties props = HierarchicalPropertiesFactory.createInstance(
                    Paths.get("src/test/resources/hierarchicalProperties/map_sample_reload.txt"), true, true);
            props.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if(evt.getPropertyName().equals("root")) {
                    System.out.println("Testing 123");
                }
            });
            Thread t = new Thread() {

                @Override
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(30000);
                            break;
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ReloadTest.class.getName()).log(Level.SEVERE, null, ex);
                            break;
                        }
                    }
                }
                
            };
            t.setDaemon(false);
            t.start();
        } catch (Exception ex) {
            Logger.getLogger(ReloadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
