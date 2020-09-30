use crate::utils;
use rand::{rngs::ThreadRng, Rng};
use std::fmt;

pub struct CPUTask {
    processing_time: u64,
}

impl CPUTask {
    #[allow(dead_code)]
    pub fn new(processing_time: u64) -> Self {
        Self { processing_time }
    }

    pub fn new_rand(rng: &mut ThreadRng) -> Self {
        let processing_time = rng.gen_range(1000, 2000);
        Self { processing_time }
    }

    pub fn run(self) {
        utils::sleep_ms(self.processing_time);
    }
}

impl fmt::Display for CPUTask {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "CPUTask(time = {} ms)", self.processing_time)
    }
}
