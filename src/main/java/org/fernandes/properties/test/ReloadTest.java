/*
 OSSCUBE 2014
 */

package org.fernandes.properties.test;

import java.beans.PropertyChangeEvent;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fernandes.properties.factory.HierarchicalPreprocessorFactory;
import org.fernandes.properties.model.HierarchicalProperties;

/**
 * This kind of test does not work with JUNIT so well. This is just a manual
 * test for the reloading of the properties. This test ends after 90 seconds.
 * So in that time you can go an change the properties file and see it being
 * reloaded.
 * @author onepoint
 */
public class ReloadTest {

    /**
     * To test this, you will need to manually change and save the file.
     * immediately after that the properties should change.
     * @param args Not used.
     */
    public static void main(String[] args) {
        try {
            HierarchicalProperties props = HierarchicalPreprocessorFactory.createInstance(
                    Paths.get("src/test/resources/hierarchicalProperties/map_sample_reload.txt"), true);
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
                            Thread.sleep(90000);
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
