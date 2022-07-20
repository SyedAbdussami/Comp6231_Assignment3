package org.Concordia;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileApiInterface extends Remote {
     void createFile(String filename, RemoteInputStream input) throws RemoteException,IOException;
     void getFileData(String hash,  RemoteOutputStream output) throws RemoteException,IOException;

//    public ArrayList<Files> listFiles() throws IOException;
}
