CPROGRAM=Pi.c
OPROGRAM=Pi.o
HOST=host_home.txt
NUMBER_OF_PROCESSOR=6

compile: ${CPROGRAM}
	mpicc -o ${OPROGRAM} ${CPROGRAM}

advanceCompile: ${CPROGRAM}
	mpicc -o ${OPROGRAM} ${CPROGRAM} -lm

execute: ${OPROGRAM} ${HOST}
	mpirun -hostfile ${HOST} -n ${NUMBER_OF_PROCESSOR} ./${OPROGRAM}