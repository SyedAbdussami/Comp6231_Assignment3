package org.Concordia;

import com.healthmarketscience.rmiio.RemoteInputStream;

import java.io.IOException;
import java.nio.file.Path;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface FileApiInterface extends Remote {
    void sendFile(String filename, RemoteInputStream input) throws RemoteException, IOException;

    void getFileData(String hash, RemoteOutputStream output) throws RemoteException, IOException;

    ArrayList<Path> listFiles() throws IOException;

    void deleteFile(String fileName) throws RemoteException, IOException;
}
