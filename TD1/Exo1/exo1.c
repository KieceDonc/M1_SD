// required MPI include file
#include "mpi.h"
#include <stdio.h>

int main(int argc, char *argv []) {
    int world_size, world_rank, len;
    char hostname[MPI_MAX_PROCESSOR_NAME];

    // initialize MPI
    MPI_Init (&argc ,&argv);

    // get number of tasks
    MPI_Comm_size(MPI_COMM_WORLD ,&world_size);

    MPI_Comm_rank(MPI_COMM_WORLD ,&world_rank);

    MPI_Get_processor_name(hostname , &len);
    // do some work with message passing
    // done with MPI

    int index = 0;

    for(int x = 0; x < 1000; x++){
        if (world_rank == 0) {
            printf("%d world from processor %s, rank %d out of %d processors\n", index, hostname, world_rank, world_size);
            index++;
            MPI_Send(&index, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
        } else if (world_rank == 1) {
            MPI_Recv(&index, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            
            printf("%d world from processor %s, rank %d out of %d processors\n", index, hostname, world_rank, world_size);
        }
    }
    MPI_Finalize ();
}