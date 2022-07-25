package org.Concordia;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IFileApi extends Remote {
  String sendFile(String filename, IRemoteInputStream input,int fileSize) throws RemoteException, IOException;

    void getFileData(String hash, IRemoteOutputStream output) throws RemoteException, IOException;

    ArrayList<String> listFiles() throws IOException;
//
    void deleteFile(String fileName) throws RemoteException, IOException;
}
