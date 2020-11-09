#ifndef __LAB6_MASTER_H__
#define __LAB6_MASTER_H__

#include <math.h>
#include <mpi.h>
#include <string.h>
#include "utils.h"

#define ARR_SIZE 1000000

void send_chunks(chunk_t* chunks, size_t nproc) {
  for (size_t i = 0, chunk_index = 0; i < nproc; i++) {
    if (i == RANK_MASTER)
      continue;
    chunk_t ch = chunks[chunk_index++];
    MPI_Send(&ch.chunk_size, 1, MPI_UINT64_T, i, MPI_TAG_UB, MPI_COMM_WORLD);
    MPI_Send(ch.arr1_start, ch.chunk_size, MPI_INT32_T, i, MPI_TAG_UB,
             MPI_COMM_WORLD);
    MPI_Send(ch.arr2_start, ch.chunk_size, MPI_INT32_T, i, MPI_TAG_UB,
             MPI_COMM_WORLD);
  }
}

void recv_chunks(chunk_t* chunks, size_t nproc, int* buf) {
  for (size_t i = 0, chunk_index = 0; i < nproc; i++) {
    if (i == RANK_MASTER)
      continue;
    chunk_t ch = chunks[chunk_index++];
    MPI_Recv(ch.result_start, ch.chunk_size, MPI_INT32_T, i, MPI_ANY_TAG,
             MPI_COMM_WORLD, NULL);
  }
}

int master_proc() {
  srand(time(NULL));

  // Initialize arrays
  int* arr1 = NEW(int, ARR_SIZE);
  int* arr2 = NEW(int, ARR_SIZE);
  int* result = NEW(int, ARR_SIZE);
  shuffle_arr(arr1, ARR_SIZE);
  shuffle_arr(arr2, ARR_SIZE);

  size_t nproc = get_comm_size();
  size_t nworkers = nproc - 1;
  printf("<M>: there are %zu workers\n", nworkers);

  // Split into chunks
  chunk_t* chunks = split_arr(arr1, arr2, result, ARR_SIZE, nworkers);
  send_chunks(chunks, nproc);
  printf("<M>: chunks sent\n");

  recv_chunks(chunks, nproc, result);
  printf("<M>: chunks received\n");

  free(arr1);
  free(arr2);
  free(result);

  return MPI_SUCCESS;
}

#endif
