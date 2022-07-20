package org.Concordia;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.Remote;

public class RemoteOutputStream extends OutputStream implements Remote {
    private OutputStream output;
    public RemoteOutputStream(OutputStream output) {
        this.output = output;
    }
    @Override
    public void write(int b) throws IOException {
        this.output.write(b);
    }

    // ... implement other wrapper methods
}

