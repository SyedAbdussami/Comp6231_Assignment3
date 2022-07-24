package org.Concordia;

import java.io.IOException;
import java.rmi.Remote;

public interface IRemoteInputStream extends Remote, AutoCloseable {
    int read() throws IOException;

    int available() throws IOException;

    void close() throws IOException;

    int read(byte[] b, int off, int len) throws IOException;
    // TODO: other function, if necessary
}
