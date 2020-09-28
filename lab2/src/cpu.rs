use crate::cpuqueue::CPUQueue;
use std::thread;

pub struct CPU {
    n: usize,
}

impl CPU {
    pub fn new(n: usize) -> Self {
        CPU { n }
    }

    pub fn run(self, q1: CPUQueue, q2: CPUQueue) {
        thread::spawn(move || {
            let mut _n = 0;
            let (mtx1, cvar1) = &*q1.monitor.clone();
            let (mtx2, cvar2) = &*q2.monitor.clone();

            // Run process generation
            q1.simulate();
            q2.simulate();

            loop {
                let task = if _n == self.n - 1 {
                    let i = {
                        let mut lck = cvar2
                            .wait_while(mtx2.lock().unwrap(), |g| g.len() == 0)
                            .unwrap();
                        let i = lck.pop_front().unwrap();
                        i
                    };
                    println!("CPU:\tRunning {} task from Queue2.", &i);
                    i
                } else {
                    let i = {
                        let mut lck = cvar1
                            .wait_while(mtx1.lock().unwrap(), |g| g.len() == 0)
                            .unwrap();
                        let i = lck.pop_front().unwrap();
                        i
                    };
                    println!("CPU:\tRunning {} task from Queue1.", &i);
                    i
                };

                task.run();
                _n = (_n + 1) % self.n;
            }
        })
        .join()
        .unwrap();
    }
}
