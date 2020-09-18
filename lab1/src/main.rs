use rand::Rng;
use std::{
    env,
    thread::{self, JoinHandle},
    time,
};

pub fn rand_array(size: usize) -> Vec<i32> {
    let mut rng = rand::thread_rng();
    (0..size)
        .map(|_| rng.gen_range(0i32, size as i32))
        .collect()
}

// Sequential sum
fn seq_sum(v1: &Vec<i32>, v2: &Vec<i32>) -> (Vec<i32>, u128) {
    let t = time::Instant::now();
    let result = v1.iter().zip(v2.iter()).map(|(&i, &j)| i + j).collect();
    (result, t.elapsed().as_micros())
}

// Parallel sum
fn par_sum(v1: &Vec<i32>, v2: &Vec<i32>, nthreads: usize) -> (Vec<i32>, u128) {
    let zipped = v1
        .iter()
        .zip(v2.iter())
        .map(|(&a, &b)| (a, b))
        .collect::<Vec<(i32, i32)>>();

    let chunk_size = (zipped.len() as f64 / nthreads as f64).ceil() as usize;
    let t = time::Instant::now();
    let chunks = zipped.chunks(chunk_size).map(|chunk| chunk.to_owned());
    let handles: Vec<JoinHandle<Vec<i32>>> = chunks
        .map(|chunk| {
            thread::spawn(move || chunk.iter().map(|(a, b)| a + b).collect())
        })
        .collect();

    let mut result: Vec<i32> = Vec::with_capacity(zipped.len());
    for handle in handles {
        let computed_chunk = handle.join().unwrap();
        result.extend(computed_chunk);
    }
    (result, t.elapsed().as_micros())
}

fn main() {
    let arr_size = {
        let s = env::args()
            .skip(1)
            .next()
            .expect("Please provide vector size.");
        s.parse::<usize>().unwrap()
    };
    let nthread_samples = [2, 4, 8, 12, 16];

    let arr1 = rand_array(arr_size);
    let arr2 = rand_array(arr_size);
    println!("Generated arrays.");

    let (seq_result, seq_time) = seq_sum(&arr1, &arr2);
    println!("Sequential calculation time: {}μs\n", seq_time);

    for &nthreads in nthread_samples.iter() {
        let (par_result, par_time) =
            par_sum(&arr1, &arr2, nthreads as usize - 1);
        assert_eq!(par_result, seq_result);
        println!(
            "{} threads:\n  Calculation time: {}μs\n  k = {}\n",
            nthreads,
            par_time,
            seq_time as f64 / par_time as f64,
        );
    }
}
