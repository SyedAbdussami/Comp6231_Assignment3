package org.Concordia;

import mpi.MPI;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileAPI implements IFileApi {

    Map<String,ArrayList<Integer>> directory = new HashMap<>();
    int fileChunkSize=1024;
    ArrayList<Integer> nodes=new ArrayList<>();
    Map<String,String> fileHashList=new HashMap<>();
    private static Integer callno = 0;

    public String sendFile(String fileName, IRemoteInputStream input,int fileSize) throws RemoteException, IOException {
       ArrayList<Integer> nodesForTheFile=new ArrayList<>();
       String hash=fileHash(fileName);
       fileHashList.put(fileName,hash);
       byte[]hashBuffer=hash.getBytes();
        int cn;
            synchronized (callno) {
                cn = ++callno;
            }
       int fileRemaining=fileSize;
       int nodeCounter=1,chunkCount=0;
       while (fileRemaining>0&&fileRemaining>fileChunkSize){
           System.out.println("File Chunk "+chunkCount+" ----------------------------------------------------------");
           byte[]buff1=new byte[fileChunkSize];
//           input.read(buff1,filePointer,fileChunkSize);
           for (int i=0;i<fileChunkSize;i++){
               buff1[i]= (byte) input.read();
           }
//           System.out.println("Read Buffer :" + new String(buff1));
           //MPI call to data node
//           System.out.println(RMIServer.MPI_PROXY);
           RMIServer.MPI_PROXY.Send(buff1,0, buff1.length, MPI.BYTE,nodeCounter,cn);
           RMIServer.MPI_PROXY.Send(hashBuffer,0, hashBuffer.length, MPI.BYTE,nodeCounter,cn);
//           RMIServer.MPI_PROXY.Sendrecv(buff1, 0, buff1.length, MPI.CHAR, nodeCounter, 0,buffer, 0, buffer.length, MPI.INT, nodeCounter,0);
           System.out.println("Data sent to Data node "+nodeCounter+" with hash :"+new String(hashBuffer));
           nodesForTheFile.add(nodeCounter);
           directory.put(fileName,nodesForTheFile);
           nodeCounter++;
           chunkCount++;
           fileRemaining-=fileChunkSize;
           System.out.println("Next chunk -------------------------------------------------------");
       }
       if(fileRemaining>0){
           byte[]buff1=new byte[fileRemaining];
           for (int i=0;i<fileRemaining;i++){
               buff1[i]= (byte) input.read();
           }
           nodesForTheFile.add(nodeCounter);
           directory.put(fileName,nodesForTheFile);
           System.out.println("Last chunk :" );
           System.out.println("Read Buffer of last chunk :" + new String(buff1));
           //Mpi call
           RMIServer.MPI_PROXY.Send(buff1,0, buff1.length, MPI.BYTE,nodeCounter,cn);
           RMIServer.MPI_PROXY.Send(hashBuffer,0, hashBuffer.length, MPI.BYTE,nodeCounter,cn);
       }
       return hash;
    }

    public void getFileData(String fileName, IRemoteOutputStream output) throws RemoteException, IOException {
        // the idea is similar

//        String data = "This is COMP6231 course assignment 3 example";
//        byte[] dataBytes = data.getBytes();

        ArrayList<Integer> nodes=directory.get(fileName);
        System.out.println("Nodes : "+nodes);
        byte[] buffer=new byte[1024];
        byte[]fileBuffer=new byte[4096];
        int filePointer=0;
        byte[] fileNameInBytes=fileHashList.get(fileName).getBytes();
        System.out.println("File name to be downloaded: "+new String(fileNameInBytes));
        for(int i=1;i<nodes.size();i++){
            RMIServer.MPI_PROXY.Sendrecv(fileNameInBytes,0,fileNameInBytes.length,MPI.BYTE,nodes.get(i),1,buffer,0,buffer.length,MPI.BYTE,nodes.get(i),1);
            for (byte b : buffer) {
                fileBuffer[filePointer++] = b;
            }
        }
        try {
            output.write(fileBuffer);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Data is written to the file.");
    }

     public ArrayList<String> listFiles()throws IOException{
        //traverse the hashmap
         ArrayList<String> fileList=new ArrayList<>();
         System.out.println("Printing file list");
         for (Map.Entry<String,ArrayList<Integer>> entry : directory.entrySet()) {
             System.out.println(entry.getKey()+" : "+entry.getValue());
             fileList.add(entry.getKey());
         }
         return fileList;

    }

    public void deleteFile(String fileName) throws IOException {
//        String fileHash=fileHash(fileName).substring(1,25);
        boolean flag=false;
//        for (Map.Entry<String,ArrayList<Integer>> entry : directory.entrySet()) {
//          if(entry.getKey().contains(fileHashList.get(fileName))){
//              flag=true;
//              break;
//          }
//        }
        if(!fileHashList.containsKey(fileName)){
            System.out.println("Incorrect File name. Please try again");
            throw new FileNotFoundException("File not found");
        }
        //MPI Delete Call
        String deleteCmd="DeleteFile_"+fileHashList.get(fileName);
        byte[] buffer=deleteCmd.getBytes();
        for (Integer node : nodes) {
            System.out.println("Sending File Deletion Request to Data Nodes");
            RMIServer.MPI_PROXY.Send(buffer, 0, buffer.length, MPI.BYTE, node, 1);
        }
        System.out.println("File Deleted at all nodes");

    }

    String fileHash(String fileName){
        String localTime = String.valueOf(java.time.LocalTime.now());
        String fileHash = DigestUtils.sha256Hex(fileName+localTime).substring(1,25);

//        System.out.println("File hash: "+fileHash);
        return fileHash;
    }


    // other API funcitons i.e. file info, lsit files, etc.
}
