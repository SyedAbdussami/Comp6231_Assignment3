package org.Concordia;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteOutputStream extends UnicastRemoteObject implements IRemoteOutputStream {
    private OutputStream output;

    public RemoteOutputStream(OutputStream output) throws RemoteException {
        super();
        this.output = output;
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.output.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.output.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }

    // ... implement other wrapper methods
}
