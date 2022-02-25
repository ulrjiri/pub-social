# Simple Calculator

Hi, I'm Jiri Ulrich from the For Developers channel. In this video, I will create a simple interpreter of
basic arithmetical expression. I will use this example to demonstrate a couple of concepts:


First, I will show you how to convert the expression to a syntax tree. Then, I will show you how to create a grammar 
which will define what are the allowed combinations in the input.
I will also use the example to show you what is the associativity and what is the priority of operators.

Then, we proceed to the real coding. We will split the input string into lexical elements, so-called tokens. We will 
use simple state automaton for this task. I will also show you how to convert inout string into integer number in 
effective and efficient way. In the last step we will create the interpreter which will be based on the grammar defined 
in the previous step.


So let's get started.


## Examples

Let's look at few examples.

```text
1 + 1
1+ 2+3 - 4
1 + 2*3 - 4
1 + 2 + 3*4*5/6 + 7
```

For simplicity, the interpreter will support integer numbers only. It will support operation ADD, SUBTRACT, MULTIPLY
and DIVIDE. It is also supposed to ignore while spaces. It will support neither floating-point numbers nor brackets 
nor unary minus.


## Associativity of Operators

Let's look at the following expression:
```text
1+2+3
```

The result is equal to 6 but how shall the interpreter proceed? Shall it be: (1+2)+3 or 1+(2+3)? Both options can be 
correct. It depends on the grammar you choose. The grammar will define what will be the associativity of the operators. 
The less-natural associativity from right  leads to easier grammar.


How this would be reflected in abstract syntax tree?


```text
1+2
        +
       / \
      1   2
```


Now let's look at more complex example.
Let me add brackets for the demonstration purpose:

```text

1+2+3 = (1+2)+3 - LEFT associativity
        +
       / \
      +   3
     / \
    1   2
```

```text
1+2+3 = 1+(2+3) - RIGHT associativity
        +
       / \
      1   +
         / \
        2   3
```

## Priority of Operators

Next topic is the priority of the operators. The question is what operator should be evaluated first? PLUS or the STAR?
```text
1+2*3
```
Shall the result be equal to 9 or equal to 7?
The correct answer is 7! Why? Because the START operator has higher priority than the PLUS operator.
For better understanding, we can again add brackets:
```text
1+2*3 will get interpreted as 1+(2*3)

        +
       / \
      1   *
         / \
        2   3
```

Of course, for the STAR operator, we can again define associativity from left or from right. In our case, we will
again chose the more natural associativity from right:

```text
1+2+3
        *
       / \
      1   *
         / \
        2   3
```

## Grammar

Simple grammar could look like this:

```text
ADD -> MUL
    -> MUL + ADD
    -> MUL - ADD
MUL -> ELE
    -> ELE * MUL
    -> ELE / MUL
ELE -> number
```

It is very readable. The associativity is from right to left.
We could try to rewrite it the following way:

```text
ADD -> MUL
    -> ADD + MUL
    -> ADD - MUL
MUL -> ELE
    -> MUL * ELE
    -> MUL / ELE
ELE -> number
```

In this case, the problem is that there is a left recursion.
So let us try another approach:

```text
ADD     -> MUL ADD_RES
ADD_RES -> + MUL ADD_RES
        -> - MUL ADD_RES
        -> empty
MUL     -> ELE MUL_RES
MUL_RES -> * ELE ADD_RES
        -> / ELE ADD_RES
        -> empty
ELE     -> number
```


Enough of theory. Let's go coding.
