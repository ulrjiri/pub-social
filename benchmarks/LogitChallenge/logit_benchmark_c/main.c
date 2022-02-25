#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<sys/time.h>

#define ACCURACY_ABS 0.000001
#define ACCURACY_REL 0.0001

typedef struct Logit {
	double prob;
	double logit;
} Logit;

double logit(double p) {
    return log(p/(1.0 - p));
}

double logistic(double a) {
	double exp_a = exp(a);
	return exp_a/(exp_a + 1);
}

char *format_time(char *str, const time_t *sec) {
	strftime(str, 80, "%Y-%m-%d %H:%M:%S", localtime(sec));
	return str;
}

int main(int arvc, char * argv[]) {
		
	long no_repeat, sample_size;
	scanf("%ld\n%ld", &no_repeat, &sample_size);
	Logit *data = (Logit *) malloc(sample_size*sizeof(Logit));		
	for (int i = 0; i < sample_size; i++) {
		double prob, logi;		
		scanf("%lf %lf", &prob, &logi);
		data[i].prob = prob;
		data[i].logit = logi;
	}
	
	struct timeval start_time;
	gettimeofday(&start_time, NULL);
	
	long run_count = 0, err_count = 0;  	
	for (int r = 0; r < no_repeat; r++) { 
		for (int i = 0; i < sample_size; i++) {
			++run_count;						
			double prob = data[i].prob, logi = data[i].logit;
			double logi_res = logit(prob);			
			double prob_res = logistic(logi_res);			
			if ((abs(logi_res - logi) >= ACCURACY_ABS && abs(logi_res - logi)/logi >= ACCURACY_REL) 
			 || (abs(prob_res - prob) >= ACCURACY_ABS && abs(prob_res - prob)/prob >= ACCURACY_REL)) {
				++err_count;
				printf("%.8lf %.8lf -> %.8lf %.8lf\n", prob, logi, prob_res, logi_res);			
			}
		}
	}
	
	struct timeval end_time;
	gettimeofday(&end_time, NULL);
    
	free(data);

	char buf[80];
	printf("C Start:%s\n", format_time(buf, &start_time.tv_sec));
	printf("C End:%s\n", format_time(buf, &end_time.tv_sec));
    printf("C Elapsed: %.6f\n", (end_time.tv_sec + end_time.tv_usec/1000000.0) - (start_time.tv_sec + start_time.tv_usec/1000000.0));

    if (!err_count) {
        printf("C OK #%ld\n", run_count);
    } else {
        printf("C ERROR #%ld(/%ld)\n", err_count, run_count);
	}
		
	return 0;
}