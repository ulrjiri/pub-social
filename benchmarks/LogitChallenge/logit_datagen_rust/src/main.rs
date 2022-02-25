use rand::Rng;

fn logit(p: f64) -> f64 {
    return (p / (1.0_f64 - p)).ln();
}

fn main() {
	let arg_no_repeat = std::env::args().nth(1).unwrap_or("1".to_string());
	let arg_sample_size = std::env::args().nth(2).unwrap_or("10".to_string());
	
	let no_repeat = arg_no_repeat.trim().parse::<i64>().unwrap();
	let sample_size = arg_sample_size.trim().parse::<i64>().unwrap();
	
    println!("{}", no_repeat);
    println!("{}", sample_size);
	
    let mut rng = rand::thread_rng();
    for _ in 0..sample_size {
        let p: f64 = rng.gen::<f64>();
        println!("{:.10} {:.10}", p, logit(p));
    }
}
