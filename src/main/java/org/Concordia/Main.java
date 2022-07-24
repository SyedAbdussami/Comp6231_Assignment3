package org.Concordia;
import mpi.*;

import java.io.IOException;

public class Main {
    static final int MASTER = 0;
    static final int DEST = 1;
    public static void main(String[] args) throws IOException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();//Number of processes or nodes
        System.out.println("Hello world from rank " + myrank + " of " + size);
        if(size < 2) {
            System.out.println("MPI size too low");
            return;
        }
        System.out.println("Hello world from <"+myrank+"> of <"+size+">");
        if(myrank == MASTER) {
            System.out.println("Master node Started");
            MasterNode.main(args);
        }
        if(myrank>=1) {
            System.out.println("Data Node started");
            DataNode.main(args);
        }
        MPI.Finalize();
    }
}