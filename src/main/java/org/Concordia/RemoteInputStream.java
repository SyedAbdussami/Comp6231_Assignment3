package org.Concordia;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteInputStream extends UnicastRemoteObject implements IRemoteInputStream, Remote {
    private InputStream input;

    public RemoteInputStream(java.io.InputStream input) throws RemoteException {
        super();
        this.input = input;
    }

    @Override
    public int read() throws IOException {
        return input.read();
    }

    @Override
    public int available() throws IOException {
        return this.input.available();
    }

    @Override
    public void close() throws IOException {
        this.input.close();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return input.read(b, off, len);
    }

    // ... implement other wrapper methods
}
