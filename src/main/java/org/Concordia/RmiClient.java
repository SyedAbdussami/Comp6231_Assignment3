package org.Concordia;

import com.healthmarketscience.rmiio.SimpleRemoteInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Starting the client");
        try
        {
            Registry registry= LocateRegistry.getRegistry();
            FileApiInterface server = (FileApiInterface) registry.lookup("FileService");
            SimpleRemoteInputStream  istream=new SimpleRemoteInputStream(new FileInputStream("src/main/resources/SampleFile.txt"));
            server.sendFile("SampleFile.txt",istream.export());

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
