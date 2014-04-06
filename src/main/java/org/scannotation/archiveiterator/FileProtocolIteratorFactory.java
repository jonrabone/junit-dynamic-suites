package org.scannotation.archiveiterator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

// This class patches a bug in Scannotation 1.0.3 as found in Maven Central.

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 2 $
 */
public class FileProtocolIteratorFactory implements DirectoryIteratorFactory {
    public StreamIterator create(URL url, Filter filter) throws IOException {
        File f;
        try {
            f = new File(url.toURI().getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (f.isDirectory()) {
            return new FileIterator(f, filter);
        } else {
            return new JarIterator(url.openStream(), filter);
        }
    }
}