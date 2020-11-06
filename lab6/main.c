#include <mpi.h>
#include "master.h"
#include "utils.h"
#include "worker.h"

int main(int argc, char* argv[]) {
  MPI_Init(&argc, &argv);
  int rank, proc_result;
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  if (rank == RANK_MASTER) {
    proc_result = master_proc();
  } else {
    proc_result = worker_proc();
  }

  MPI_Finalize();
  return proc_result;
}
