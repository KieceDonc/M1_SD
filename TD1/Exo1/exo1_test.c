// required MPI include file
#include "mpi.h"
#include <stdio.h>

int main(int argc , char *argv []) {
    int numtasks , rank , len , rc;
    char hostname[MPI_MAX_PROCESSOR_NAME];

    // initialize MPI
    MPI_Init (&argc ,&argv);

    MPI_Status status;

    // get number of tasks
    MPI_Comm_size(MPI_COMM_WORLD ,&numtasks);

    // get my rank
    MPI_Comm_rank(MPI_COMM_WORLD ,&rank);

    MPI_Send("test",4,MPI_CHAR,1,MPI_ANY_TAG,MPI_COMM_WORLD);
    MPI_Recv("test",4,MPI_CHAR,0,MPI_ANY_TAG,MPI_COMM_WORLD,&status);
    
    // do some work with message passing
    // done with MPI
    MPI_Finalize ();
}