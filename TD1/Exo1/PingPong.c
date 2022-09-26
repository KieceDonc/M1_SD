// required MPI include file
#include "mpi.h"
#include <stdio.h>

int main(int argc , char *argv []) {
    int world_size, world_rank, processor_name_length;
    char processor_name[MPI_MAX_PROCESSOR_NAME];

    // initialize MPI
    MPI_Init (&argc ,&argv);

    // get number of tasks
    MPI_Comm_size(MPI_COMM_WORLD ,&world_size);

    // get my rank
    MPI_Comm_rank(MPI_COMM_WORLD ,&world_rank);

    MPI_Get_processor_name(processor_name, &processor_name_length);


    int msg_size = 100;
    char msg[msg_size];
    if(world_rank == 0){
        strcat(msg,"Pong");
        printf("Ping from processor %s, rank %d out of %d processors\n",
        processor_name, world_rank, world_size);
        MPI_Send(&msg,msg_size,MPI_CHAR,1,0,MPI_COMM_WORLD);
    }else if (world_rank == 1){
        MPI_Recv(&msg,msg_size,MPI_CHAR,0,0,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
        printf("%s from processor %s, rank %d out of %d processors\n",
           msg, processor_name, world_rank, world_size);
    }    
    // do some work with message passing
    // done with MPI
    MPI_Finalize ();
}