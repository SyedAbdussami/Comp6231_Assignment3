package org.Concordia;

import mpi.MPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataNode {
    public static void main(String[] args) throws IOException {
        System.out.println("Data node up " + args[0] + " and running");
        System.out.println("Creating File Directory");
        try{
            Path path = Paths.get("src/main/resources/" + "DataNode_" + args[0] + "_Directory");
            Files.createDirectories(path);
        }catch (Exception ex){
            System.out.println("Exception occurred while directory");
        }
        byte[]buffer=new byte[1024];
        MPI.COMM_WORLD.Recv(buffer,0, buffer.length, MPI.BYTE,0,1);
//        File file=new File("src/main/resources/" + "DataNode_" + args[0] +"_Directory/");


        System.out.println("Recieved data : "+new String(buffer));

    }
}
