#ifndef __LAB6_UTILS_H__
#define __LAB6_UTILS_H__

#include <math.h>
#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define RANK_MASTER 0
#define NEW(type, len) ((type*)malloc(len * sizeof(type)))
#define FOR(i, start, end) for (size_t i = start; i < end; ++i)
#define MIN(a, b) ((a < b) ? a : b)

void print_arr(int* arr, size_t len) {
  char* buf = NEW(char, len * 6 + 2);
  buf[0] = '[';
  FOR(i, 0, len) { sprintf((buf + i * 6) + 1, "%4d, ", arr[i]); }
  buf[len * 6] = ']';
  printf("%s\n", buf);
  free(buf);
}

void rand_array(int* buf, size_t length) {
  FOR(i, 0, length) { buf[i] = rand() % length; }
}

typedef struct {
  int* arr1_start;
  int* arr2_start;
  int chunk_size;
} chunk_t;

chunk_t* split_arr(int* arr1, int* arr2, size_t arr_size, int nproc) {
  int chunk_size = (int)ceil(arr_size / (double)nproc);
  int chunk_index = 0;
  chunk_t* chunks = NEW(chunk_t, nproc);
  do {
    int offset = chunk_size * chunk_index;
    chunks[chunk_index] =
        (chunk_t){.arr1_start = arr1 + offset,
                  .arr2_start = arr2 + offset,
                  .chunk_size = MIN(chunk_size, arr_size - offset)};
    chunk_index++;
  } while (chunk_index < nproc);
  return chunks;
}

void print_chunk(chunk_t* chunk, int* arr1, int* arr2) {
  printf("start1 = %p, start2 = %p, length = %d\n", chunk->arr1_start,
         chunk->arr2_start, chunk->chunk_size);
  print_arr(chunk->arr1_start, chunk->chunk_size);
  print_arr(chunk->arr2_start, chunk->chunk_size);
  printf("\n");
}

#endif
