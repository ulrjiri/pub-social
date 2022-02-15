# Simple Calculator

## Overview

Simple evaluator for mathematical expressions +, -, *, / and ^ (power) with backets () and unary minus. 

Sample expressions:
```
1.02 + -9.02 + (1.2 * 4.034) * -2 * -4.4 + -(3 + 6)
4*-(((5+5)+(2*5))+5)
-(3 + 6)
```

There is no error handling.

## Priority of Operators 
- () brackets (highers priority)
- ^ power 
- *, / multiplicative expressions
- +, - additive expressions (lowest priority)

## Grammar
```
ADD -> MUL
    -> MUL + ADD
    -> MUL - ADD
MUL -> POW
    -> POW * MUL
    -> POW / MUL
POW -> ELEM
    -> ELEM ^ POW
ELE -> number
    -> UMIN number
    -> LBRA ADD RBRA
    -> UMIN LBRA ADD RBRA
```

## Lexer

Simple status automaton lexer.

Recognizes numbers, opertors, brackets, unary minus. 

Ignores white spaces. 

### Methods

show() - show the next symbol (but keep it in the input)

go() - read next symbol and procees to next one


## Evaluators (Interpreters)

### Stack-Based

Uses two stacks - one for numbers and one for operators.

There is a special mark for brackets 'M'.

This evaluator is very efficient.

### Grammar-Based

Uses methods derived from the grammar. 

In fact, this interpreter is also using stack. In runtime, it is using standard Java method stack instead of special "object" stack as in previous case.  

Very readable, can be mechanically very easily derived from the grammar.


### AST-Based (Abstract Systax Tree-Based)

Example of AST (abstract syntax tree):
```
  8*2 + 7 - 3*4

        +
       / \
      /   \
     /     \
    *        -
   / \      / \
  /   \    /   \
 8     2  7     *
               / \
              /   \
             3     4
```

Works in two steps. In the first step, it constructs the AST. In second step, the AST is interpreted. 

Higher memory consulption compared to the previous cases. 

It is only a demo but in real examples, AST is often used to inspect and improve the logic. E.g. automated code optimization, dead code detection, compile-time precalculation and others can be done on AST. 







## 
