#!/bin/bash

#TEST_FILE=logit_data_1_10.txt
TEST_FILE=logit_data_5000_1000000.txt
NO_ITERRATIONS=4
NO_REPEAT_PER_INTERRATION=3

cd logit_benchmark_c
gcc -Wall -lm -o3 -o main main.c
cd ..

cd logit_benchmark_c++
g++ -Wall -lm -o3 -o main main.cpp
cd ..

cd logit_benchmark_go
go build main.go
cd ..

cd logit_benchmark_java
javac Main.java
cd ..

cd logit_benchmark_rust
cargo build --release
cd ..

function benchmark() 
{
	for (( r = 0; r < $NO_REPEAT_PER_INTERRATION; r++ )) 
	do
		case $1 in
			"exe"  ) $2 < $3 ;;
			"java" ) java -cp $2 $3 < $4 ;;
		esac
	done
}

for (( i = 0; i < $NO_ITERRATIONS; i++ )) 
do
	benchmark exe logit_benchmark_c/main.exe $TEST_FILE
	benchmark exe logit_benchmark_c++/main.exe $TEST_FILE
	benchmark exe logit_benchmark_go/main.exe $TEST_FILE
	benchmark java logit_benchmark_java Main $TEST_FILE
	benchmark exe logit_benchmark_rust/target/release/logit_test_rust.exe $TEST_FILE	
done
