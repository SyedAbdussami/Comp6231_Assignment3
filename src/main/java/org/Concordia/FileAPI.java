package org.Concordia;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class FileAPI implements FileApiInterface{

    Map<String, File> directory=new HashMap<>();
//    Map<String,> NodeDirectory

    public void createFile(String filename, RemoteInputStream input) throws RemoteException, IOException {
        //genertaing the hash
        String hash = DigestUtils.sha256Hex(filename);
        String localTime= String.valueOf(java.time.LocalTime.now());
        String fileHash=hash+localTime;

        byte[] buffer=new byte[4096];
        int byteRead=-1;
        try(OutputStream os=new FileOutputStream("src/main/resources/OutputFile.txt")){
            while (input.read(buffer)!=byteRead){
                os.write(buffer,0,byteRead);
            }
        }

//        input.read()
//
//
//        // calculate hash
//
//        //actual file read
//        while(input.available() > 0) {
//            // read ...
//
//        }
        //save the file id in the hashmap
        //MPI call
//        return hash;
        //divide the file into chunks
        //do the MPI send call to Data Nodes
    }

    public void getFileData(String hash,  RemoteOutputStream output) throws RemoteException, IOException {
        // the idea is similar


        //
    }

//    public ArrayList<Files> listFiles()throws IOException{
//        //traverse the hashmap
//
//
//    }

    // other API funcitons i.e. file info, lsit files, etc.
}
