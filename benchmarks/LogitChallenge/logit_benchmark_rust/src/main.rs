use chrono::Local;
use std::io;
use std::time::Instant;

const ACCURACY_ABS: f64 = 0.000001;
const ACCURACY_REL: f64 =  0.0001;

struct Logit {
    prob: f64,
    logit: f64,
}

impl Logit {
    fn new(prob: f64, logit: f64) -> Self {
        Logit {
            prob: prob,
            logit: logit,
        }
    }
}

fn logit(prob: f64) -> f64 {
    return (prob / (1.0_f64 - prob)).ln();
}

fn logistic(a: f64) -> f64 {
    let exp_a = a.exp();
	return exp_a/(exp_a + 1.0);
}

fn main() {

    let  stin = io::stdin();
    let mut buf = String::new();
    
	stin.read_line(&mut buf).unwrap();
	let no_repeat = buf.trim().parse::<i64>().unwrap();

	buf.clear();
	stin.read_line(&mut buf).unwrap();
	let sample_size = buf.trim().parse::<i64>().unwrap();
	
    let mut v = Vec::<Logit>::with_capacity(sample_size.try_into().unwrap());
    for _ in 0..sample_size {
        buf.clear();
        stin.read_line(&mut buf).unwrap();
        let mut split = buf.split(' ');
        let prob = split.next().unwrap().trim().parse::<f64>().unwrap();
        let logit = split.next().unwrap().trim().parse::<f64>().unwrap();
        v.push(Logit::new(prob, logit));
    }

    println!("Rust Start: {}", Local::now());
    let start = Instant::now();

    let mut run_count: i64 = 0;
    let mut err_count: i64 = 0;
    for _ in 0..no_repeat {
        for d in &v { 
            run_count += 1;						
            let prob = d.prob; 
            let logi = d.logit;
            let logi_res = logit(prob);			
            let prob_res = logistic(logi_res);			
            if (logi_res - logi).abs() >= ACCURACY_ABS && (logi_res - logi).abs()/logi >= ACCURACY_REL 
            || (prob_res - prob).abs() >= ACCURACY_ABS && (prob_res - prob).abs()/prob >= ACCURACY_REL {
                err_count += 1;
                println!("{} {} -> {} {}", prob, logi, prob_res, logi_res);			
            }
        }
    }

    let duration = start.elapsed();
    println!("Rust End: {}", Local::now());
    println!("Rust Elapsed: {:?}", duration);

    if err_count == 0 {
        println!("Rust OK #{}", run_count);
    } else {
        println!("Rust ERROR #{}/{}", err_count, run_count);
    }
}
