#include<vector>
#include <iostream>
#include <chrono>
#include <ctime>    
#include <cmath>  

#define ACCURACY_ABS 0.000001
#define ACCURACY_REL 0.0001

using namespace std;

class Logit {
public:
	double prob, logit;
	Logit() {}
	Logit(const Logit &l) = default;
	Logit(double prob, double logit) : prob(prob), logit(logit) {}
	Logit& operator=(Logit other) {
		other.prob = prob;
		return *this; 		
	}		
};

double logit(double p) {
    return log(p/(1.0 - p));
}

double logistic(double a) {
	double exp_a = exp(a);
	return exp_a/(exp_a + 1);
}

int main(int argc, char *argv[]) {

	long no_repeat, sample_size;
	cin >> no_repeat >> sample_size;
	
	vector<Logit> v;
	v.reserve(sample_size);

	double prob, logi;
	cin >>  prob >> logi;
	while (!cin.eof()) {
		v.push_back(Logit(prob, logi));
		cin >>  prob >> logi;
	}

	time_t t = chrono::system_clock::to_time_t(chrono::system_clock::now());
    cout << "C++ Start: " << ctime(&t);
	auto start_clock = chrono::high_resolution_clock::now();
	
	long run_count = 0, err_count = 0;
	for (int r = 0; r < no_repeat; r++) {
		for (const auto& d: v) {
			++run_count;
			prob = d.prob; 
			logi = d.logit;
			double logi_res = logit(prob);			
			double prob_res = logistic(logi_res);			
			if ((abs(logi_res - logi) >= ACCURACY_ABS && abs(logi_res - logi)/logi >= ACCURACY_REL) 
			 || (abs(prob_res - prob) >= ACCURACY_ABS && abs(prob_res - prob)/prob >= ACCURACY_REL)) {
				++err_count;
//				cout << prob << " " << logi << " -> " << prob_res << " " << logi_res << endl;
			}
		}
	}
	
    auto end_clock = chrono::high_resolution_clock::now();
	time_t t2 = chrono::system_clock::to_time_t(chrono::system_clock::now());
    cout << "C++ End: " << ctime(&t2);
    cout << "C++ Elapsed: " << chrono::duration_cast<chrono::milliseconds>(end_clock-start_clock).count()/1000.0 << "s" << endl;

    if (!err_count) {
        cout << "C++ OK #" << run_count << endl;
    } else {
        cout << "C++ ERROR #" << err_count << "(/" << run_count << ")" << endl;
	}
		
	return 0;
}