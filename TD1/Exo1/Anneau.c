// required MPI include file
#include "mpi.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

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


    int msg_size = 10;
    char msg[msg_size];

    int world_previous_rank = (world_rank - 1) % world_size;
    int world_next_rank = (world_rank + 1) % world_size;
    if(world_rank == 0){

        strcpy(msg,"Ping");
        MPI_Send(&msg,msg_size,MPI_CHAR,world_next_rank,0,MPI_COMM_WORLD);

        int index = 0;
        while(index < 4){
            MPI_Recv(&msg,msg_size,MPI_CHAR,world_previous_rank,0,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
            printf("%s from processor %s, rank %d out of %d processors, index = %d\n", msg, processor_name, world_rank, world_size, index);

            if(strcmp(msg, "Ping") == 0){
                strcpy(msg,"Pong");
                MPI_Send(&msg,msg_size,MPI_CHAR,world_next_rank,0,MPI_COMM_WORLD);
            }else{
                strcpy(msg,"Ping");
                MPI_Send(&msg,msg_size,MPI_CHAR,world_next_rank,0,MPI_COMM_WORLD);
            }
            
            index++;
        }

        strcpy(msg,"Stop");
        for(int processusIndex = 1; processusIndex < world_size; processusIndex++){
            MPI_Send(&msg,msg_size,MPI_CHAR,processusIndex,0,MPI_COMM_WORLD);
        }
    } else {
        int shouldContinue = 1;

        while(shouldContinue){
            MPI_Recv(&msg,msg_size,MPI_CHAR,MPI_ANY_SOURCE,0,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
            printf("%s from processor %s, rank %d out of %d processors\n", msg, processor_name, world_rank, world_size);
            
            if(strcmp(msg, "Stop") == 0){
                shouldContinue = 0;
            }else if(strcmp(msg, "Ping") == 0){
                strcpy(msg,"Pong");
                MPI_Send(&msg,msg_size,MPI_CHAR,world_next_rank,0,MPI_COMM_WORLD);

            }else{
                strcpy(msg,"Ping");
                MPI_Send(&msg,msg_size,MPI_CHAR,world_next_rank,0,MPI_COMM_WORLD);
            }
        }
    }    

    // do some work with message passing
    // done with MPI
    MPI_Finalize ();
} 
