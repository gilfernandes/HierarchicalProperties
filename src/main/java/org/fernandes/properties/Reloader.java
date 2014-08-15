/*
 OSSCUBE 2014
 */
package org.fernandes.properties;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import org.fernandes.properties.factory.HierarchicalPreprocessorFactory;
import org.fernandes.properties.model.HierarchicalProperties;

/**
 * Used to reload the hierarchical properties in case of need.
 *
 * @author onepoint
 */
public enum Reloader {

    /**
     * Singleton instance.
     */
    INSTANCE;

    /**
     * Reloads the properties in case the file changes on the file system.
     *
     * @param propsPath The path of the properties file.
     * @param props The properties file object itself.
     */
    public void startReloadThread(Path propsPath, HierarchicalProperties props) {

        new Thread() {

            /**
             * Watches a directory to see, if there was a modification on the
             * properties file and if there is one, simply reloads the file and
             * resets the root.
             */
            @Override
            public void run() {
                Path dir = propsPath.getParent();
                for (;;) {
                    try {
                        WatchService watcher = dir.getFileSystem().newWatchService();
                        dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
                        WatchKey watckKey = watcher.take();
                        List<WatchEvent<?>> events = watckKey.pollEvents();
                        events.stream().filter((event) -> (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)).forEach((event) -> {
                            WatchEvent.Kind<?> kind = event.kind();
                            if (kind != OVERFLOW) {
                                // The filename is the
                                // context of the event.
                                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                                Path filename = ev.context();
                                final Path fileName = propsPath.getFileName();
                                if (filename.equals(fileName)) {
                                    HierarchicalProperties reloadedProps = HierarchicalPreprocessorFactory.createInstance(propsPath);
                                    props.setRoot(reloadedProps.getRoot());
                                }
                            }
                        });
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }.start();

    }
}
