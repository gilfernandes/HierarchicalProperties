/*
 OSSCUBE 2014
 */

package org.fernandes.properties.util;

import java.io.IOException;
import java.io.Reader;

/**
 * Input output operations as a singleton.
 * @author onepoint
 */
public enum IOOperations {
    
    INSTANCE;
    
    /**
     * Copies a reader into a character sequence.
     * @param reader The reader to be read from.
     * @return 
     * @throws IOException 
     */
    public CharSequence copy(Reader reader) throws IOException {
        char[] buf = new char[4096];
        StringBuffer sb = new StringBuffer();
        while(reader.ready()) {
            int length = reader.read(buf);
            sb.append(new String(buf, 0, length));
        }
        return sb;
    }
}
