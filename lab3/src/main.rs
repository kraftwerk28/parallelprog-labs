use std::{
    sync::{atomic::AtomicI32, Arc},
    thread,
};

fn main() {
    let mut i = Arc::new(32);
    *i = 12;

    thread::spawn(move || {
        // *i = 12;
    });
    println!("Hello, world!");
}
