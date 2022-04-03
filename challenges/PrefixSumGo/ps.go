package main

import (
	"sync"
	"time"
)

// Load simulation - wait for one second
func simulateLoad() {
	time.Sleep(1 * time.Second)
}

func prefSum_linear(a *[]int32) ([]int32, float32) {
	if a == nil || *a == nil {
		return nil, 0
	}

	t_start := time.Now()

	l := len(*a)
	s := make([]int32, l)
	if l > 0 {
		s[0] = (*a)[0]
		simulateLoad()
		for i := 1; i < l; i++ {
			s[i] = s[i-1] + (*a)[i]
			simulateLoad()
		}
	}

	elapsed_time := float32(time.Since(t_start) / time.Second)
	return s, elapsed_time
}

func prefSum_divideConquerSerial(a *[]int32) ([]int32, float32) {
	if a == nil || *a == nil {
		return nil, 0
	}

	t_start := time.Now()

	l := len(*a)
	s := make([]int32, l)
	if l > 0 {
		divideConquerRecursive(a, &s, 0, l-1)
	}

	elapsed := float32(time.Since(t_start) / time.Second)
	return s, elapsed
}

func prefSum_divideConquerParallel(a *[]int32) ([]int32, float32) {
	if a == nil || *a == nil {
		return nil, 0
	}

	t_start := time.Now()

	l := len(*a)
	s := make([]int32, l)
	if l > 0 {
		divideConquerRecursiveParallel(a, &s, 0, l-1)
	}

	elapsed_time := float32(time.Since(t_start) / time.Second)
	return s, elapsed_time
}

func divideConquerRecursive(a *[]int32, s *[]int32, from int, to int) {

	// error state - do nothing
	if from > to {
		return
	}

	// trivial casewith one element only
	if from == to {
		(*s)[from] = (*a)[from]
		simulateLoad()
		return

	} else {
		// complex case: split it to two parts and call the solution recursivelly
		m := (from + to) / 2
		divideConquerRecursive(a, s, from, m)
		divideConquerRecursive(a, s, m+1, to)

		// aggregate result
		x := (*s)[m]
		for i := m + 1; i <= to; i++ {
			(*s)[i] += x
		}
	}
}

func divideConquerRecursiveParallel(a *[]int32, s *[]int32, from int, to int) {

	// error state - do nothing
	if from > to {
		return
	}

	// trivial casewith one element only
	if from == to {
		(*s)[from] = (*a)[from]
		simulateLoad()
		return

	} else {

		// complex case: split it to two parts and call the solution recursivelly in parallel
		m := (from + to) / 2
		wg := sync.WaitGroup{}
		wg.Add(2)
		go func() {
			defer wg.Done() // do not forget calling Done at the end of the routine
			divideConquerRecursiveParallel(a, s, from, m)
		}()
		go func() {
			defer wg.Done() // do not forget calling Done at the end of the routine
			divideConquerRecursiveParallel(a, s, m+1, to)
		}()
		wg.Wait() // wait until both parts are done

		// merge the results from the pararell run (left part is ok, adjust right part accordingly)
		x := (*s)[m]
		for i := m + 1; i <= to; i++ {
			(*s)[i] += x
		}
	}
}
