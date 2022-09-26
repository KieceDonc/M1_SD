CPROGRAM=Anneau.c
OPROGRAM=Anneau.o
HOST=host_home.txt
NUMBER_OF_PROCESSOR=4

compile: ${CPROGRAM}
	mpicc -o ${OPROGRAM} ${CPROGRAM}

execute: ${OPROGRAM} ${HOST}
	mpirun -n ${NUMBER_OF_PROCESSOR} -f ${HOST} ./${OPROGRAM}