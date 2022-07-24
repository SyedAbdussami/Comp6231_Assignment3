package org.Concordia;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MasterNode {
    static final int MASTER = 0;
    static final int DEST = 1;
    public static void main(String[] args) {
        try {
            FileAPI server = new FileAPI();
            IFileApi stub = (IFileApi) UnicastRemoteObject.exportObject((IFileApi) server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("FileService", stub);
            System.out.println("Service Deamon Started");
            System.out.println("Waiting for client...");

            //
        } catch (Exception ex) {
            System.err.println("Something Occurred in Server");
            ex.printStackTrace();
        }
    }
}
