use rand::Rng;
use std::{
    env,
    thread::{self, JoinHandle},
    time,
};

fn seq_sum(v1: &Vec<i32>, v2: &Vec<i32>) -> Vec<i32> {
    v1.iter().zip(v2.iter()).map(|(&i, &j)| i + j).collect()
}

pub fn rand_array(size: usize) -> Vec<i32> {
    let mut rng = rand::thread_rng();
    (0..size)
        .map(|_| rng.gen_range(0i32, size as i32))
        .collect()
}

fn par_sum(v1: &Vec<i32>, v2: &Vec<i32>, nthreads: usize) -> Vec<i32> {
    let zipped = v1
        .iter()
        .zip(v2.iter())
        .map(|(&a, &b)| (a, b))
        .collect::<Vec<(i32, i32)>>();

    let chunk_size = (zipped.len() as f64 / nthreads as f64).ceil() as usize;
    println!("Chunk size: {}", chunk_size);
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
    result
}

fn main() {
    let arr_size = {
        let s = env::args()
            .skip(1)
            .next()
            .expect("Please provide vector size.");
        s.parse::<usize>().unwrap()
    };

    let arr1 = rand_array(arr_size);
    let arr2 = rand_array(arr_size);
    println!("Generated arrays.");

    let timer = time::Instant::now();
    par_sum(&arr1, &arr2, 11);
    let par_time = timer.elapsed().as_micros();

    let timer = time::Instant::now();
    seq_sum(&arr1, &arr2);
    let seq_time = timer.elapsed().as_micros();
    println!(
        "Parallel calculation time: {}μs\nSequential calculation time: {}μs",
        par_time, seq_time,
    );
    println!("k = {}", seq_time as f64 / par_time as f64);
}
