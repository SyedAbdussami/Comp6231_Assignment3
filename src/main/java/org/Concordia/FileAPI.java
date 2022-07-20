package org.Concordia;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class FileAPI implements FileApiInterface {

    Map<String, File> directory = new HashMap<>();
//    Map<String,> NodeDirectory

    public void sendFile(String filename, RemoteInputStream input) throws RemoteException, IOException {
        //genertaing the hash
        String hash = DigestUtils.sha256Hex(filename);
        String localTime = String.valueOf(java.time.LocalTime.now());
        String fileHash = hash + localTime;

        InputStream inputStream;
        FileOutputStream fos = null;
        File serverFile;
        int chunk = 4096;
        byte[] result = new byte[chunk];
        int readBytes = 0;

        try {
            inputStream = RemoteInputStreamClient.wrap(input);
            serverFile = File.createTempFile("Temp", "txt");
            fos = new FileOutputStream(serverFile);
            do {
                readBytes = inputStream.read(result);
                if (readBytes > 0)
                    fos.write(result, 0, readBytes);
            } while (readBytes != -1);

            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                fos.close();
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

    public void getFileData(String hash, RemoteOutputStream output) throws RemoteException, IOException {
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
