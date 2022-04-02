package main

import (
	"fmt"
)

func main() {

	a := []int32{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
	w := []int32{1, 3, 6, 10, 15, 21, 28, 36, 45, 55}

	lin, lin_e := prefSum_linear(&a)
	dcs, dcs_e := prefSum_divideConquerSerial(&a)
	dcp, dcp_e := prefSum_divideConquerParallel(&a)

	fmt.Printf("Input                  : %v\n", a)
	fmt.Printf("Want                   : %v\n", w)
	fmt.Printf("Linear                 : %v (in %f sec)\n", lin, lin_e)
	fmt.Printf("Divide-Conquer Serial  : %v (in %f sec)\n", dcs, dcs_e)
	fmt.Printf("Divide-Conquer Parallel: %v (in %f sec)\n", dcp, dcp_e)
}
