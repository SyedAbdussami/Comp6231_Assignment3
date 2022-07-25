package org.Concordia;

import mpi.MPI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class DataNode {

    static Map<String, File> nodeDirectory = new HashMap<>();

    static void createFile(byte[] bytes, String filePath) {
        try {
            OutputStream os = new FileOutputStream(filePath);
            os.write(bytes);
            os.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 20 + 1);
        String filePath = "src/main/resources/" + "DataNode_" + args[0] + "_Directory/file_" + randomNum + ".txt";
        System.out.println("Data node up " + args[0] + " and running");
        System.out.println("Creating File Directory");
        try {
            Path path = Paths.get("src/main/resources/" + "DataNode_" + args[0] + "_Directory");
            Files.createDirectories(path);
        } catch (Exception ex) {
            System.out.println("Exception occurred while directory");
        }
        byte[] buffer = new byte[1024];
        MPI.COMM_WORLD.Recv(buffer, 0, buffer.length, MPI.BYTE, 0, 1);
//        System.out.println("Received Data at Node " + args[0] + ":" + new String(buffer));
        createFile(buffer, filePath);
        System.out.println("File created at Data Node "+args[0]);
        MPI.COMM_WORLD.Recv(buffer, 0, buffer.length, MPI.BYTE, 0, 1);
        String fileHash = new String(buffer);
        System.out.println("Received file Hash at Node "+args[0] +" = "+fileHash);
        nodeDirectory.put(fileHash, new File(filePath));
        System.out.println("Node "+args[0]+" directory");
        nodeDirectory.forEach((key, value) -> System.out.println("hash :"+key + ": " + ":file. "+value));
//        int[] num=new int[1];
//        num[0]=1;
//        MPI.COMM_WORLD.Send(num,0, num.length, MPI.INT,0,1);
//        System.out.println("Recieved data : "+new String(buffer));

        System.out.println("Waiting for master's Request for file retrieval");
        MPI.COMM_WORLD.Recv(buffer, 0, buffer.length, MPI.BYTE, 0, 1);
        System.out.println("Download Req received");
        nodeDirectory.forEach((key,value)->{
            if(Objects.equals(key, new String(buffer))){
                File file=nodeDirectory.get(key);
                try {
                    InputStream in=new FileInputStream(file);
                    byte[] readBytes=new byte[(int)file.length()];
                    for (int i=0;i<readBytes.length;i++){
                        readBytes[i]= (byte) in.read();
                    }
                    RMIServer.MPI_PROXY.Send(readBytes,0, readBytes.length, MPI.BYTE,0,1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
