// required MPI include file
#include "mpi.h"
#include <stdio.h>

int main(int argc, char *argv []) {
    int world_size, world_rank, len, index;
    char hostname[MPI_MAX_PROCESSOR_NAME];

    // initialize MPI
    MPI_Init (&argc ,&argv);

    // get number of tasks
    MPI_Comm_size(MPI_COMM_WORLD ,&world_size);

    MPI_Comm_rank(MPI_COMM_WORLD ,&world_rank);

    MPI_Get_processor_name(hostname , &len);
    // do some work with message passing
    // done with MPI

    char msg[100];
    if(world_rank == 0){
        while(index < 100) {
            index++;
            if(strcmp(msg,"Pong") == 0){
                strcpy(msg,"Ping");
            }else{
                strcpy(msg,"Pong");
            }

            MPI_Send(&msg, 1, MPI_CHAR, (world_rank + 1) % world_size, 0, MPI_COMM_WORLD); 
        }
    }else{
        MPI_Recv(&msg, 1, MPI_CHAR, (world_rank - 1) % world_size, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        if(strcmp(msg,"Pong") == 0){
            strcpy(msg,"Ping");
        }else if(strcmp(msg,"1") == 0){
            strcpy(msg,"Pong");
        }else{
            
        }
    }

    MPI_Finalize ();
}