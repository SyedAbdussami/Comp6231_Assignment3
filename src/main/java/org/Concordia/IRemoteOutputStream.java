package org.Concordia;

import java.io.IOException;
import java.rmi.Remote;

public interface IRemoteOutputStream extends Remote,AutoCloseable {
    void write(byte[] b) throws IOException;

    void write(byte[] b, int off, int len) throws IOException;

    void close() throws IOException;
    // TODO: other function, if necessary
}
