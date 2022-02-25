package main

import (
	"fmt"
	"math"
	"time"
)

const ACCURACY_ABS float64 = 0.000001
const ACCURACY_REL float64 = 0.0001

type Logit struct {
	prob  float64
	logit float64
}

func logit(p float64) float64 {
	return math.Log(p / (1.0 - p))
}

func logistic(a float64) float64 {
	var exp_a = math.Exp(a)
	return exp_a / (exp_a + 1)
}

func main() {

	var no_repeat int64
	var sample_size int64
	fmt.Scanf("%d", &no_repeat)
	fmt.Scanf("%d", &sample_size)

	data := make([]Logit, sample_size)
	for i := 0; i < len(data); i++ {
		fmt.Scanf("%f", &data[i].prob)
		fmt.Scanf("%f", &data[i].logit)
	}

	t_start := time.Now()

	var run_count int64 = 0
	var err_count int64 = 0
	var r int64
	for r = 0; r < no_repeat; r++ {
		for i := 0; i < len(data); i++ {
			run_count++
			var prob = data[i].prob
			var logi = data[i].logit
			var logi_res = logit(prob)
			var prob_res = logistic(logi_res)
			if (math.Abs(logi_res-logi) >= ACCURACY_ABS && math.Abs(logi_res-logi)/logi >= ACCURACY_REL) || (math.Abs(prob_res-prob) >= ACCURACY_ABS && math.Abs(prob_res-prob)/prob >= ACCURACY_REL) {
				err_count++
				fmt.Printf("%.8f %.8f -> %.8f %.8f\n", prob, logi, prob_res, logi_res)
			}
		}
	}

	elapsed := time.Since(t_start)
	t_end := time.Now()

	fmt.Printf("Go Start:%s\n", t_start.String())
	fmt.Printf("Go End:%s\n", t_end.String())
	fmt.Printf("Go Elapsed: %f s\n", float64(elapsed)/1.0e9)

	if err_count == 0 {
		fmt.Printf("Go OK #%d\n", run_count)
	} else {
		fmt.Printf("Go ERROR #%d(/%d)\n", err_count, run_count)
	}
}
