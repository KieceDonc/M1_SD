// required MPI include file
#include "mpi.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

int isIndiceCircle(double x, double y){
    return pow(x,2)+pow(y,2) <= 1;
}

double genNumber() {
    return (rand() + 1.0) / (RAND_MAX+2.0);
}

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

    if(world_size < 2){
        printf("Not enough processor (must be equal or higher than 2)\n");
        exit(0);
    }

    int msg = 0;
    int points_number = 10000000;

    if(world_rank == 0){

        int points_inside_circle = 0;

        for(int processusIndex = 1; processusIndex < world_size; processusIndex++){
            int toCount = (int) (points_number / (world_size - 1));
            
            if(processusIndex == world_size - 1){
                toCount = points_number + toCount - toCount * (world_size - 1 );
            }

            printf("Processus %d have %d points\n", processusIndex, toCount);


            MPI_Send(&toCount,1,MPI_INT,processusIndex,0,MPI_COMM_WORLD);
        }

        int remaining_processus = world_size - 1;
        while(remaining_processus > 0){
            MPI_Recv(&msg,1,MPI_INT,MPI_ANY_SOURCE,0,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
            points_inside_circle += msg;
            remaining_processus--;
        }


        double pi = 4 * points_inside_circle / (double)points_number;
        printf("Pi = %f\n",pi);
    } else {
        srand(world_rank);
        MPI_Recv(&msg,1,MPI_INT,0,0,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
        int toCount = msg;
        printf("Processus %d received %d points to count\n", world_rank, toCount);

        int points_inside_circle = 0;

        for(int index = 0; index < toCount; index++){
            double coordinate_x = genNumber();
            double coordinate_y = genNumber();
            if(isIndiceCircle(coordinate_x,coordinate_y)){
                points_inside_circle++;
            }
        }
        
        printf("Processus %d, %d points inside circle\n", world_rank, points_inside_circle);

        MPI_Send(&points_inside_circle,1,MPI_INT,0,0,MPI_COMM_WORLD);
    }

    // do some work with message passing
    // done with MPI
    MPI_Finalize ();
}