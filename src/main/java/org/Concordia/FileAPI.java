package org.Concordia;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileAPI implements FileApiInterface {

    Map<String, Path> directory = new HashMap<>();
//    Map<String,> NodeDirectory

    public void sendFile(String fileName, RemoteInputStream input) throws RemoteException, IOException {
        InputStream inputStream;
        FileOutputStream fos = null;
        int chunk = 4096;
        byte[] buffer = new byte[chunk];
        int readBytes = -1;
        String text_file="src/main/resources/"+fileName+"_At_Master_"+fileHash(fileName)+".txt";
        try {
            inputStream = RemoteInputStreamClient.wrap(input);
            Path textFilePath = Paths.get(text_file);
            fos = new FileOutputStream(String.valueOf(Files.createFile(textFilePath)));
            while ((readBytes=inputStream.read(buffer))!=-1){
                //MPI  send to data nodes
                //Either send the buffer array or the file chunk by converting it into file
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                fos.close();
        }
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

    public void getFileData(String hash, RemoteOutputStream output) throws RemoteException, IOException {
        // the idea is similar
    }

     public ArrayList<Path> listFiles()throws IOException{
        //traverse the hashmap
         ArrayList<Path> fileList=new ArrayList<>();
         for (Map.Entry<String,Path> entry : directory.entrySet()) {
             System.out.println(entry.getKey()+" : "+entry.getValue());
             fileList.add(entry.getValue());
         }

         return fileList;

    }

    public void deleteFile(String fileName) throws FileNotFoundException {
        String fileHash=fileHash(fileName).substring(1,25);
        boolean flag=false;
        for (Map.Entry<String,Path> entry : directory.entrySet()) {
          if(entry.getKey().contains(fileHash)){
              flag=true;
              break;
          }
        }
        if(!flag){
            throw new FileNotFoundException("File not found");
        }
        //MPI Delete Call
    }

    String fileHash(String fileName){
        String localTime = String.valueOf(java.time.LocalTime.now());
        String fileHash = DigestUtils.sha256Hex(fileName+localTime).substring(1,25);

        System.out.println("File hash: "+fileHash);
        return fileHash;
    }


    // other API funcitons i.e. file info, lsit files, etc.
}
