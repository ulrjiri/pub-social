package main

import (
	"math/rand"
	"reflect"
	"runtime"
	"testing"
)

func getFunctionName(temp interface{}) string {
	return runtime.FuncForPC(reflect.ValueOf(temp).Pointer()).Name()
}

func TestPrefSumFunctionality(t *testing.T) {

	functions := [](func(*[]int32) ([]int32, float32)){
		prefSum_linear,
		prefSum_divideConquerSerial,
		prefSum_divideConquerParallel,
	}

	testcases := []struct {
		inp, want []int32
	}{
		{nil, nil},
		{[]int32{}, []int32{}},
		{[]int32{1}, []int32{1}},
		{[]int32{1, 2}, []int32{1, 3}},
		{[]int32{1, 2, 3}, []int32{1, 3, 6}},
		{[]int32{1, 2, 3, 4}, []int32{1, 3, 6, 10}},
		{[]int32{1, 2, 3, 4, 5}, []int32{1, 3, 6, 10, 15}},
		{[]int32{1, 2, 3, 4, 5, 6}, []int32{1, 3, 6, 10, 15, 21}},
	}

	for _, fnc := range functions {
		t.Logf("Function: %s\n", getFunctionName(fnc))
		for _, tc := range testcases {
			calc, elaps := fnc(&tc.inp)
			t.Logf("  Input size: %d, Elapsed: %f sec\n", len(tc.inp), elaps)
			if !reflect.DeepEqual(calc, tc.want) {
				t.Errorf("Function: %s, Input: %v, Want: %v, Calc: %v\n", getFunctionName(fnc), tc.inp, tc.want, calc)
			}
		}
	}
}

func TestPrefSumParallelSpeed(t *testing.T) {

	const TEST_SIZE = 32
	const SEED = 123456
	const MAX_VALUE = 1000

	rand.Seed(SEED)
	a := make([]int32, TEST_SIZE)
	for i := 0; i < len(a); i++ {
		a[i] = rand.Int31n(MAX_VALUE)
	}

	lin, lin_e := prefSum_linear(&a)
	dcs, dcs_e := prefSum_divideConquerSerial(&a)
	dcp, dcp_e := prefSum_divideConquerParallel(&a)

	t.Logf("Input                  : %v\n", a)
	t.Logf("Linear                 : %v (in %f sec)\n", lin, lin_e)
	t.Logf("Divide-Conquer Serial  : %v (in %f sec)\n", dcs, dcs_e)
	t.Logf("Divide-Conquer Parallel: %v (in %f sec)\n", dcp, dcp_e)

	if !reflect.DeepEqual(dcs, lin) {
		t.Errorf("Divide-Conquer Serial output different from Linear output.\n")
	}
	if !reflect.DeepEqual(dcp, lin) {
		t.Errorf("Divide-Conquer Parallel output different from Linear output.\n")
	}

	if dcs_e < lin_e/1.25 || lin_e*1.25 < dcs_e {
		t.Errorf("Calculation time of Divide-Conquer Serial should be similar to calculation time of Linear\n")
	}
	if dcp_e > lin_e/float32(len(a))*1.25 {
		t.Errorf("Calculation time of Divide-Conquer Parallel should faster than Linear approx. size-of-the-input-times (%d)\n", len(a))
	}
}
