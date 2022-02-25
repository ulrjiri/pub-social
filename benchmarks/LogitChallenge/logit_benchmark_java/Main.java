import java.lang.Math;
import java.util.Scanner;
import java.time.LocalDateTime;   
import java.util.Vector;

public class Main {   

	static class Logit {
		public double prob, logit;
		public Logit(double prob, double logit) {
			this.prob = prob;
			this.logit = logit;
		}
	}
	
	private static double ACCURACY_ABS = 0.000001;
	private static double ACCURACY_REL = 0.0001;
	
	private static double logit(double p) {
		return Math.log(p/(1.0 - p));
	}

	private static double logistic(double a) {
		double exp_a = Math.exp(a);
		return exp_a/(exp_a + 1);
	}

	public static void main (String[] args) {
		
		Scanner scanner = new Scanner(System.in);

		long noRepeat = Long.parseLong(scanner.nextLine());
		long sampleSize = Long.parseLong(scanner.nextLine());

		Vector<Logit> v = new Vector<>((int) sampleSize);

		for (int i = 0; i < sampleSize; i++) {
			String[] split = scanner.nextLine().split(" ");
			v.add(new Logit(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
		}
		
		System.out.println("Java Start: " + LocalDateTime.now());
		long startTime = System.currentTimeMillis();
		
		long run_count = 0, err_count = 0;
		for (int r = 0; r < noRepeat; r++) {
			for (Logit d : v) {
				++run_count;
				double prob = d.prob, logi = d.logit;
				double logi_res = logit(prob);			
				double prob_res = logistic(logi_res);			
				if (Math.abs(logi_res - logi) >= ACCURACY_ABS && Math.abs(logi_res - logi)/logi >= ACCURACY_REL 
				 || Math.abs(prob_res - prob) >= ACCURACY_ABS && Math.abs(prob_res - prob)/prob >= ACCURACY_REL) {
					++err_count;
					System.out.printf("%.8f %.8f -> %.8f %.8f\n", prob, logi, prob_res, logi_res);			
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Java End: " + LocalDateTime.now());
		
		System.out.printf("Java Elapsed: %.6fs\n", (endTime - startTime)/1000.0);
		
		if (err_count == 0) {
			System.out.printf("Java OK #%d\n", run_count);
		} else {
			System.out.printf("Java ERROR #%d(/%d)\n", err_count, run_count);
		}
	}	
}