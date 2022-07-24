package org.Concordia;

import mpi.MPI;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileAPI implements IFileApi {

    Map<String,ArrayList<Integer>> directory = new HashMap<>();
    int fileChunkSize=1024;
    ArrayList<Integer> nodes=new ArrayList<>();

    public String sendFile(String fileName, IRemoteInputStream input,int fileSize) throws RemoteException, IOException {
       ArrayList<Integer> nodesForTheFile=new ArrayList<>();
       String hash=fileHash(fileName);
       byte[]buffer=new byte[fileSize];
       int fileRemaining=fileSize;
       int nodeCounter=1,chunkCount=0;
//       String recvNodeAck;
       while (fileRemaining>0&&fileRemaining>fileChunkSize){
           System.out.println("File Chunk "+chunkCount+" ---------------------------");
           byte[]buff1=new byte[fileChunkSize];
//           input.read(buff1,filePointer,fileChunkSize);
           for (int i=0;i<fileChunkSize;i++){
               buff1[i]= (byte) input.read();
           }
           System.out.println("Read Buffer :" + new String(buff1));
           //MPI call to data node
           RMIServer.MPI_PROXY.Send(buff1,0, buff1.length, MPI.BYTE,nodeCounter,1);
           System.out.println("Data sent to Data node "+nodeCounter);
           nodesForTheFile.add(nodeCounter);
           directory.put(fileName,nodesForTheFile);
           nodeCounter++;
           chunkCount++;
           fileRemaining-=fileChunkSize;
           System.out.println("Next chunk -------------------------------------------------------");
       }
//        System.out.println();
       if(fileRemaining>0){
           byte[]buff1=new byte[fileRemaining];
           for (int i=0;i<fileRemaining;i++){
               buff1[i]= (byte) input.read();
           }
           System.out.println("Last chunk :" );
           System.out.println("Read Buffer :" + new String(buff1));
           //Mpi call
           RMIServer.MPI_PROXY.Send(buff1,0, buff1.length, MPI.BYTE,nodeCounter,1);
       }
       return hash;
    }

    public void getFileData(String hash, IRemoteOutputStream output) throws RemoteException, IOException {
        // the idea is similar

        String data = "This is COMP6231 course assignment 3 example.dvgdgbdfh";
        byte[] dataBytes = data.getBytes();

        try {
            output.write(dataBytes);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Data is written to the file.");
    }

//     public ArrayList<Path> listFiles()throws IOException{
//        //traverse the hashmap
//         ArrayList<Path> fileList=new ArrayList<>();
//         for (Map.Entry<String,Path> entry : directory.entrySet()) {
//             System.out.println(entry.getKey()+" : "+entry.getValue());
//             fileList.add(entry.getValue());
//         }
//
//         return fileList;
//
//    }
//
//    public void deleteFile(String fileName) throws FileNotFoundException {
//        String fileHash=fileHash(fileName).substring(1,25);
//        boolean flag=false;
//        for (Map.Entry<String,Path> entry : directory.entrySet()) {
//          if(entry.getKey().contains(fileHash)){
//              flag=true;
//              break;
//          }
//        }
//        if(!flag){
//            throw new FileNotFoundException("File not found");
//        }
//        //MPI Delete Call
//    }

    String fileHash(String fileName){
        String localTime = String.valueOf(java.time.LocalTime.now());
        String fileHash = DigestUtils.sha256Hex(fileName+localTime).substring(1,25);

        System.out.println("File hash: "+fileHash);
        return fileHash;
    }


    // other API funcitons i.e. file info, lsit files, etc.
}
