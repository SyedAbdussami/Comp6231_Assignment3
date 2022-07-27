package org.Concordia;

import mpi.MPI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
        byte[] fileHashBuffer = new byte[24];
        System.out.println("File created at Data Node " + args[0]);
        MPI.COMM_WORLD.Recv(fileHashBuffer, 0, fileHashBuffer.length, MPI.BYTE, 0, 1);
        String fileHash = new String(fileHashBuffer);
        System.out.println("Received file Hash at Node " + args[0] + " = " + fileHash);
        nodeDirectory.put(fileHash, new File(filePath));
        System.out.println("Node " + args[0] + " directory");
        nodeDirectory.forEach((key, value) -> System.out.println("hash :" + key + ":file. " + value));
//        int[] num=new int[1];
//        num[0]=1;
//        MPI.COMM_WORLD.Send(num,0, num.length, MPI.INT,0,1);
//        System.out.println("Recieved data : "+new String(buffer));

        System.out.println("Data Node " + args[0] + " Waiting for master's Request for file retrieval");
        Arrays.fill(buffer, (byte) 0);
        byte[] hash = new byte[24];
        MPI.COMM_WORLD.Recv(hash, 0, hash.length, MPI.BYTE, 0, 1);
        System.out.println("Download Req received by data node " + args[0] + " for hash " + new String(hash));
        System.out.println(nodeDirectory.get(new String(hash)));
        nodeDirectory.forEach((key, value) -> {
            System.out.println("--" + key);
            System.out.println("-" + value);
            if (Objects.equals(key, new String(hash))) {
                System.out.println("File chunk found at Data node " + args[0]);
                File file = nodeDirectory.get(key);
                try {
                    InputStream in = new FileInputStream(file);
                    byte[] readBytes = new byte[(int) file.length()];
                    for (int i = 0; i < readBytes.length; i++) {
                        readBytes[i] = (byte) in.read();
                    }
                    System.out.println("Read buffer for sending back ::::" + new String(readBytes));
                    MPI.COMM_WORLD.Send(readBytes, 0, readBytes.length, MPI.BYTE, 0, 1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        byte[] deleteFileBuffer = new byte[40];
        MPI.COMM_WORLD.Recv(deleteFileBuffer, 0, deleteFileBuffer.length, MPI.BYTE, 0, 1);
        String s = new String(deleteFileBuffer);
        if (s.contains("DeleteFile_")) {
            String deleteFileHash = s.split("_")[1];
            System.out.println("Received request to delete file chunk with hash : " + deleteFileHash);
            if (nodeDirectory.containsKey(deleteFileHash)) {
                System.out.println("File chunk exist in the node " + args[0] +"at "+ nodeDirectory.get(deleteFileHash));
                File deleteFile = nodeDirectory.get(deleteFileHash);
                if (deleteFile.delete()) {
                    System.out.println("File Chunk deleted at node "+args[0]);
                }else {
                    System.out.println("Error deleting the file");
                }

            }
        }
    }
}
