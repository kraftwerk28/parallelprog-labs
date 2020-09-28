use std::{env, thread, time::Duration};
pub fn sleep_ms(millis: u64) {
    thread::sleep(Duration::from_millis(millis));
}

const USAGE: &str = "Usage: lab2 <n> <queue1-size>";

pub fn parse_args() -> (usize, usize) {
    let arglist = env::args()
        .skip(1)
        .take(2)
        .map(|v| v.parse().unwrap())
        .collect::<Vec<usize>>();
    arglist
        .get(0)
        .and_then(|&n| arglist.get(1).and_then(|&q| Some((n, q))))
        .expect(USAGE)
}
