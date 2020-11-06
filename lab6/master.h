#ifndef __LAB6_MASTER_H__
#define __LAB6_MASTER_H__

#include <math.h>
#include <mpi.h>
#include <string.h>
#include "utils.h"

#define ARR_SIZE 30000000

int master_proc() {
  srand(time(0));
  // Initialize arrays
  int* arr1 = NEW(int, ARR_SIZE);
  int* arr2 = NEW(int, ARR_SIZE);
  int* result = NEW(int, ARR_SIZE);

  int nproc;
  MPI_Comm_size(MPI_COMM_WORLD, &nproc);
  int nworkers = nproc - 1;

  rand_array(arr1, ARR_SIZE);
  rand_array(arr2, ARR_SIZE);
  printf("There are %d workers\n", nworkers);

  // Split into chunks
  chunk_t* chunks = split_arr(arr1, arr2, ARR_SIZE, nworkers);

  for (int i = 0, chunk_index = 0; i < nproc; i++) {
    if (i == RANK_MASTER)
      continue;
    chunk_t ch = chunks[chunk_index++];
    MPI_Send(&ch.chunk_size, 1, MPI_INT32_T, i, MPI_TAG_UB, MPI_COMM_WORLD);
    MPI_Send(ch.arr1_start, ch.chunk_size, MPI_INT32_T, i, MPI_TAG_UB,
             MPI_COMM_WORLD);
    MPI_Send(ch.arr2_start, ch.chunk_size, MPI_INT32_T, i, MPI_TAG_UB,
             MPI_COMM_WORLD);
  }
  printf("Payloads sent\n");

  for (int i = 0, chunk_index = 0; i < nproc; i++) {
    if (i == RANK_MASTER)
      continue;
    chunk_t ch = chunks[chunk_index++];
    int* res_ptr = ch.arr1_start - arr1 + result;
    MPI_Recv(res_ptr, ch.chunk_size, MPI_INT32_T, i, MPI_ANY_TAG,
             MPI_COMM_WORLD, NULL);
  }

  free(arr1);
  free(arr2);
  free(result);

  return MPI_SUCCESS;
}

#endif
