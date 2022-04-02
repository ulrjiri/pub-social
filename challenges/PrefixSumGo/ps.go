package main

import (
	"sync"
	"time"
)

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

	elapsed := float32(time.Since(t_start) / time.Second)
	return s, elapsed
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

	elapsed := float32(time.Since(t_start) / time.Second)
	return s, elapsed
}

func divideConquerRecursive(a *[]int32, s *[]int32, from int, to int) {
	if from > to {
		return
	}
	if from == to {
		(*s)[from] = (*a)[from]
		simulateLoad()
		return
	}
	m := (from + to) / 2
	divideConquerRecursive(a, s, from, m)
	divideConquerRecursive(a, s, m+1, to)
	x := (*s)[m]
	for i := m + 1; i <= to; i++ {
		(*s)[i] += x
	}
}

func divideConquerRecursiveParallel(a *[]int32, s *[]int32, from int, to int) {
	if from > to {
		return
	}

	if from == to {
		(*s)[from] = (*a)[from]
		simulateLoad()
		return
	}

	m := (from + to) / 2

	wg := sync.WaitGroup{}
	wg.Add(2)
	go func() {
		defer wg.Done()
		divideConquerRecursiveParallel(a, s, from, m)
	}()
	go func() {
		defer wg.Done()
		divideConquerRecursiveParallel(a, s, m+1, to)
	}()
	wg.Wait()

	x := (*s)[m]
	for i := m + 1; i <= to; i++ {
		(*s)[i] += x
	}
}
