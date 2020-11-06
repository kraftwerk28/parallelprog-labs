#ifndef __LAB6_WORKER_H__
#define __LAB6_WORKER_H__

#include <mpi.h>
#include "utils.h"

int worker_proc() {
  int chunk_size;
  MPI_Recv(&chunk_size, 1, MPI_INT32_T, RANK_MASTER, MPI_ANY_TAG,
           MPI_COMM_WORLD, NULL);
  int* arr1 = NEW(int, chunk_size);
  int* arr2 = NEW(int, chunk_size);
  MPI_Recv(arr1, chunk_size, MPI_INT32_T, RANK_MASTER, MPI_ANY_TAG,
           MPI_COMM_WORLD, NULL);
  MPI_Recv(arr2, chunk_size, MPI_INT32_T, RANK_MASTER, MPI_ANY_TAG,
           MPI_COMM_WORLD, NULL);
  FOR(i, 0, chunk_size) { arr1[i] = arr1[i] + arr2[i]; }
  MPI_Send(arr1, chunk_size, MPI_INT32_T, RANK_MASTER, MPI_TAG_UB,
           MPI_COMM_WORLD);
  free(arr1);
  free(arr2);
  return MPI_SUCCESS;
}

#endif
