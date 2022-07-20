package org.Concordia;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MasterNode {
    public static void main(String[] args) {
        try {
            FileAPI server = new FileAPI();
            FileApiInterface stub = (FileApiInterface) UnicastRemoteObject.exportObject((FileApiInterface) server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("FileService", stub);
            System.out.println("Service Deamon Started");
            System.out.println("Waiting for client...");
        } catch (Exception ex) {
            System.err.println("Something Occurred in Server");
            ex.printStackTrace();
        }
    }
}
