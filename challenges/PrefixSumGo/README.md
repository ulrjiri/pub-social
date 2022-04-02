# Prefix Sum

Problem described here:

[https://en.wikipedia.org/wiki/Prefix_sum](https://en.wikipedia.org/wiki/Prefix_sum)

## Load Simulation

In order to demonstrate effects of parallel calculation, load has to be simulated. Artificat load is put to the reading from the input array. It simulates the load needed to prepare each element.

See the simulateLoad() method in ps.go file.

## Linear

Simple linear calculation. Used as etalon. Most efficient way to achive the goal.

## Divide and Conquer

Approahc described here:

[https://en.wikipedia.org/wiki/Divide-and-conquer_algorithm](https://en.wikipedia.org/wiki/Divide-and-conquer_algorithm)

It is just a demonstration of the approach. With the load simulation, significant run-time reductions can be achieved.
Recursivelly split the problem is smaller pieces and then aggregates sub-results.

The approach to apply divide and conquer method to the prefix sum problem is:

1. If there is only one element in the input, then the sum is equal to this value.

2. If the input is longer than one character, devidie the input into two parts: (1 .. length/2) and (length/2 + 1 .. length). Apply recursivelly the same algorithm to both parts.

3. The total sum for the first (left) part is equal to the values returned. The total sum for the second (right) part has to be increased by the value in the last sum in the left part (element at index [length/2]). E.g.:

```txt
element[length/2 + 1] = element[length/2 + 1] + element[length/2] 
element[length/2 + 2] = element[length/2 + 2] + element[length/2]
... 
element[length      ] = element[length      ] + element[length/2]
```

### Serial

No synchronization needed. Recursive algorithm, but the parts of the split problem run sequentially. No benefits can be achieved.

### Parallel

Real parallel procession. Leads to significant reduction of runtime.

## Visual Studio Code Setup

Edit the .vscode/settings.json file so that the test timeout is increased and the test is verbose (t.LogF output will be visible).

```json
{
   "go.testTimeout": "180s",
    "go.testFlags": [
        "-v"
    ]
}
```
