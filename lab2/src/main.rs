use crate::{cpu::CPU, cpuqueue::CPUQueue, utils::parse_args};

mod cpu;
mod cpuprocess;
mod cpuqueue;
mod cputask;
mod utils;

fn main() {
    let (n, max_q1_size) = parse_args();

    let q1 = CPUQueue::new(Some(max_q1_size), "Queue1");
    let q2 = CPUQueue::new(None, "Queue2");
    let cpu = CPU::new(n);

    cpu.run(q1, q2);
}
