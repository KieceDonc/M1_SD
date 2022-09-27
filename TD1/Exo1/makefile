CPROGRAM=Anneau.c
OPROGRAM=Anneau.o
HOST=host_home.txt
NUMBER_OF_PROCESSOR=3

compile: ${CPROGRAM}
	mpicc -o ${OPROGRAM} ${CPROGRAM}

execute: ${OPROGRAM} ${HOST}
	mpirun -hostfile ${HOST} -n ${NUMBER_OF_PROCESSOR} ./${OPROGRAM}