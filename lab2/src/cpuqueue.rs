use crate::{cputask::CPUTask, utils::sleep_ms};
use rand::thread_rng;
use std::{
    collections::VecDeque,
    sync::{
        atomic::{AtomicBool, Ordering::Acquire},
        Arc, Condvar, Mutex,
    },
    thread::{spawn, JoinHandle},
};

type Monitor = Arc<(Mutex<VecDeque<CPUTask>>, Condvar)>;

pub struct CPUQueue {
    pub discarded_tasks: usize,
    pub monitor: Monitor,

    fixed_size: Option<usize>,
    label: &'static str,
}

impl CPUQueue {
    pub fn new(fixed_size: Option<usize>, label: &'static str) -> Self {
        let queue = match fixed_size {
            Some(s) => VecDeque::with_capacity(s),
            None => VecDeque::new(),
        };
        let queue_mtx = Mutex::new(queue);
        Self {
            monitor: Arc::new((queue_mtx, Condvar::new())),
            fixed_size,
            discarded_tasks: 0,
            label,
        }
    }

    pub fn simulate(mut self, stop_flag: Arc<AtomicBool>) -> JoinHandle<()> {
        spawn(move || {
            let (mtx, cvar) = &*self.monitor;
            let mut rng = thread_rng();

            loop {

                if stop_flag.load(Acquire) {
                    println!("Stopping {}", self.label);
                    if let Some(_) = self.fixed_size {
                        println!(
                            "{}: Discarded tasks: {}",
                            self.label, self.discarded_tasks
                        );
                    }
                    println!(
                        "{}: Queue size: {}",
                        self.label,
                        mtx.lock().unwrap().len()
                    );
                    break;
                }

                // Simulate task creation
                sleep_ms(1000);
                let new_task = CPUTask::new_rand(&mut rng);

                let mut lck = mtx.lock().unwrap();

                if let Some(max_size) = self.fixed_size {
                    if lck.len() > max_size {
                        println!("{}:\tExceeded max queue size.", self.label);
                        self.discarded_tasks += 1;
                        continue;
                    }
                }

                println!("{}:\tCreated task: {}.", self.label, &new_task);
                lck.push_back(new_task);
                cvar.notify_one();
            }
        })
    }
}
