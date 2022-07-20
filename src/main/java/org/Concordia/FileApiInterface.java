package org.Concordia;

import com.healthmarketscience.rmiio.RemoteInputStream;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileApiInterface extends Remote {
     void sendFile(String filename, RemoteInputStream input) throws RemoteException,IOException;
     void getFileData(String hash,  RemoteOutputStream output) throws RemoteException,IOException;

//    public ArrayList<Files> listFiles() throws IOException;
}
