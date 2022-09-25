CPROGRAM=exo1_test.c
OPROGRAM=exo1_test.o
HOST=host_home.txt
NUMBER_OF_PROCESSOR=2

compile: ${CPROGRAM}
	mpicc -o ${OPROGRAM} ${CPROGRAM}

execute: ${OPROGRAM} ${HOST}
	mpirun -n ${NUMBER_OF_PROCESSOR} -f ${HOST} ./${OPROGRAM}