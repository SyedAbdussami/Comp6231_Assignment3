package org.Concordia;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RmiClient {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Starting the client");
        try {
            Registry registry= LocateRegistry.getRegistry();
            IFileApi server = (IFileApi) registry.lookup("FileService");
//            IFileApi server = (IFileApi) Naming.lookup("rmi://localhost/FileService");
            System.out.println("Remote Object Found");
//            System.out.println(server);
            File file = new File("src/main/resources/SampleFile.txt");
            FileInputStream fis = new FileInputStream(file);
            byte[] fileByteArray = fis.readAllBytes();
            InputStream is = new ByteArrayInputStream(fileByteArray);
            try (IRemoteInputStream fileData = (IRemoteInputStream) new RemoteInputStream(is)) {
                String hash = server.sendFile(file.getName(), fileData, (int) file.length());
//                System.out.println("File hash: " + hash);
                System.out.println("File lists");
                ArrayList<String> files=server.listFiles();
                System.out.println("File list: "+files);
                OutputStream out = new FileOutputStream("src/main/resources/OutputFile.txt");
                IRemoteOutputStream outputStream = (IRemoteOutputStream) new RemoteOutputStream(out);
                server.getFileData("SampleFile.txt", outputStream);
            }

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
