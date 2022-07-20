package org.Concordia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Starting the client");
        InputStream inputStream=new FileInputStream("src/main/resources/SampleFile.txt");
        RemoteInputStream ris=new RemoteInputStream(inputStream);
        try
        {
            Registry registry= LocateRegistry.getRegistry();
            FileApiInterface server = (FileApiInterface) registry.lookup("FileService");
            server.createFile("SampleFile.txt",ris);

        } catch (RemoteException e) {
            System.out.println("Registry exception");
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            System.out.println("lookup exception");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
