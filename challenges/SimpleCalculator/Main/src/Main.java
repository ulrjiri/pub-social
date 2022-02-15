//----------------------------------------------------------------------------------------------------------------------
// LEXER
//----------------------------------------------------------------------------------------------------------------------

enum TokenType {
    LBRACKET, RBRACKET, NUMBER, OPERATOR, UNARYMINUS
}


class Token {
    TokenType type;
    double value;
    char operator;

    public Token(TokenType type, double value, char operator) {
        this.type = type;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public String toString() {
        String s = "Token{ type=" + type;
        if(TokenType.NUMBER == type)
            s += ", value=" + value;
        else
            s += ", operator=" + operator;
        s += " }";
        return s;
    }
}


class Lexer {
    private int index;
    private final char[] array;
    private Token lastToken = null;
    private Token nextToken = null;

    public Lexer(String expr) {
        array = expr.toCharArray();
        index = 0;
    }

    private boolean isNumeric() {
        return index < array.length && array[index] >= '0' && array[index] <= '9';
    }

    private boolean isOperator() {
        return array[index] == '+' || array[index] == '-' || array[index] == '*' || array[index] == '/' || array[index] == '^';
    }

    private double readNumber() {
        double tmp = array[index] - '0';
        ++index;
        while(isNumeric())
            tmp = (tmp * 10) + array[index++];
        if(index < array.length && array[index] == '.') {
            ++index;
            double dec = 0, order = 10;
            while(isNumeric()) {
                dec += (array[index] - '0') / order;
                order *= 10;
                ++index;
            }
            tmp += dec;
        }
        return tmp;
    }

    Token go() {
        if (nextToken != null) {
            lastToken = nextToken;
            nextToken = null;
            return lastToken;
        }

        if(index == array.length) {
            lastToken = null;
            return null;
        }

        while(index < array.length && array[index] == ' ')
            ++index;

        if(isNumeric()) {
            lastToken = new Token(TokenType.NUMBER, readNumber(), '0');
        } else if (isOperator()) {
            if(array[index] == '-' && index < array.length - 1) {
                ++index;
                if((lastToken == null || lastToken.type == TokenType.OPERATOR) &&  (isNumeric() || array[index] == '(')) {
                    lastToken = new Token(TokenType.UNARYMINUS, -1, '-');
                } else
                    lastToken = new Token(TokenType.OPERATOR, -1, '-');
            } else
                lastToken = new Token(TokenType.OPERATOR, -1, array[index++]);
        } else if(array[index] == '(') {
            lastToken = new Token(TokenType.LBRACKET, -1, array[index++]);
        } else if(array[index] == ')') {
            lastToken = new Token(TokenType.RBRACKET, -1, array[index++]);
        }

        return lastToken;
    }

    Token show() {
        if (nextToken == null) {
            Token tmp = lastToken;
            nextToken = go();
            lastToken = tmp;
        }
        return nextToken;
    }
}


//----------------------------------------------------------------------------------------------------------------------
// Evaluators
//----------------------------------------------------------------------------------------------------------------------

interface Evaluator {
    double evaluate(String expression);
}


//----------------------------------------------------------------------------------------------------------------------
// Evaluate the calculator expression using stack
//----------------------------------------------------------------------------------------------------------------------

class StackEvaluator implements Evaluator {
    private static Double evalBinOp(Double pop, Double pop1, Character operator) {
        switch (operator) {
            case '+': return pop + pop1;
            case '-': return pop - pop1;
            case '*': return pop * pop1;
            case '/': return pop / pop1;
            case '^': return Math.pow(pop,pop1);
        }
        return null;
    }

    private static boolean precedence(char operator, Character peek) {
        return (peek == '^' && operator != '^')
            || (peek == '*' || peek == '/') && (operator == '+' || operator == '-');
    }

    public double evaluate(String expression) {
        java.util.Stack<Double> numbers = new java.util.Stack<>();
        java.util.Stack<Character> operators = new java.util.Stack<>();

        Lexer lexer = new Lexer(expression);
        Token token = lexer.go();
        while(token != null) {

            if(token.type == TokenType.NUMBER) {
                numbers.push(token.value);
            } else if (token.type == TokenType.UNARYMINUS) {
                token = lexer.go();
                if (token.type == TokenType.NUMBER)
                    numbers.push(-1*token.value);
                else {
                    numbers.push(-1.0);
                    operators.push('*');
                    operators.push('M');
                }
            } else if (token.type == TokenType.OPERATOR) {
                while(!operators.empty() && precedence(token.operator, operators.peek())) {
                    numbers.push(evalBinOp(numbers.pop(), numbers.pop(), operators.pop()));
                }
                operators.push(token.operator);
            } else if (token.type == TokenType.LBRACKET) {
                operators.push('M'); // MARK
            } else if (token.type == TokenType.RBRACKET) {
                while (operators.peek() != 'M')
                    numbers.push(evalBinOp(numbers.pop(), numbers.pop(), operators.pop()));
                operators.pop();
            }
            token = lexer.go();
        }

        while(!operators.empty()) {
            numbers.push(evalBinOp(numbers.pop(), numbers.pop(), operators.pop()));
        }

        return numbers.pop();
    }
}


//----------------------------------------------------------------------------------------------------------------------
// Evaluate the calculator expression using the following grammar
//----------------------------------------------------------------------------------------------------------------------
// Grammar:
// --------------------------
// ADD -> MUL
//     -> MUL + ADD
//     -> MUL - ADD
// MUL -> POW
//     -> POW * MUL
//     -> POW / MUL
// POW -> ELEM
//     -> ELEM ^ POW
// ELE -> number
//     -> UMIN number
//     -> LBRA ADD RBRA
//     -> UMIN LBRA ADD RBRA
//----------------------------------------------------------------------------------------------------------------------
class GrammarEvaluator implements Evaluator {
    private Lexer lexer;

    private double evalADD() {
        double mul = evalMUL();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR && (t.operator == '+' || t.operator == '-')) {
            t = lexer.go();
            double add = evalADD();
            if (t.operator == '+')
                return mul+add;
            else
                return mul-add;
        } else
            return mul;
    }

    private double evalMUL() {
        double pow = evalPOW();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR && (t.operator == '*' || t.operator == '/')) {
            t = lexer.go();
            double mul = evalMUL();
            if (t.operator == '*')
                return pow*mul;
            else
                return pow/mul;
        } else
            return pow;
    }

    private double evalPOW() {
        double ele = evalELEM();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR && t.operator == '^') {
            lexer.go();
            double pow = evalPOW();
            return Math.pow(ele,pow);
        } else
            return ele;
    }

    private double evalELEM() {
        Token t = lexer.go();
        if (t.type == TokenType.NUMBER) {
            return t.value;
        } else if (t.type == TokenType.UNARYMINUS) {
            Token u = lexer.go();
            if (u.type == TokenType.NUMBER)
                return -u.value;
            else if (u.type == TokenType.LBRACKET) {
                double a = evalADD();
                lexer.go(); // RBRACKET
                return -a;
            }
        } else if (t.type == TokenType.LBRACKET) {
            double a = evalADD();
            lexer.go();  // RBRACKET
            return a;
        }
        return t.value;
    }

    public double evaluate(String expression) {
        lexer = new Lexer(expression);
        return evalADD();
    }
}


//----------------------------------------------------------------------------------------------------------------------
// Construct AST (abstract syntax tree) and interpret it
//----------------------------------------------------------------------------------------------------------------------
//                                                 8*2 + 7 - 3*4
//
// ADD -> MUL                                            +
//     -> MUL + ADD                                     / \
//     -> MUL - ADD                                    /   \
// MUL -> POW                                         /     \
//     -> POW * MUL                                  *        -
//     -> POW / MUL                                 / \      / \
// POW -> ELEM                                     /   \    /   \
//     -> ELEM ^ POW                              8     2  7     *
// ELE -> number                                                / \
//     -> UMIN number                                          /   \
//     -> LBRA ADD RBRA                                       3     4
//     -> UMIN LBRA ADD RBRA
//----------------------------------------------------------------------------------------------------------------------

interface ASTNode {
    double evaluate();
}

class NumberNode implements ASTNode {
    private final double number;

    public NumberNode(double number) {
        this.number = number;
    }

    @Override
    public double evaluate() {
        return number;
    }
}

class OpNode implements ASTNode {
    private final char operation;
    private final ASTNode left, right;

    public OpNode(char operation, ASTNode left, ASTNode right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }


    @Override
    public double evaluate() {
        switch (operation) {
            case '+': return left.evaluate() + right.evaluate();
            case '-': return left.evaluate() - right.evaluate();
            case '*': return left.evaluate() * right.evaluate();
            case '/': return left.evaluate() / right.evaluate();
            case '^': return Math.pow(left.evaluate(), right.evaluate());
        }
        return 0; // this is error -> should not happen if expression is ok
    }
}

class TreeEvaluator implements Evaluator {
    private Lexer lexer;

    private ASTNode evalADD() {
        ASTNode mul = evalMUL();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR && (t.operator == '+' || t.operator == '-')) {
            lexer.go();
            return new OpNode(t.operator, mul, evalADD());
        } else
            return mul;
    }

    private ASTNode evalMUL() {
        ASTNode pow = evalPOW();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR && (t.operator == '*' || t.operator == '/')) {
            t = lexer.go();
            return new OpNode(t.operator, pow, evalMUL());
        } else
            return pow;
    }

    private ASTNode evalPOW() {
        ASTNode ele = evalELEM();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR && t.operator == '^') {
            lexer.go();
            return new OpNode(t.operator, ele, evalPOW());
        } else
            return ele;
    }

    private ASTNode evalELEM() {
        Token t = lexer.go();
        if (t.type == TokenType.NUMBER) {
            return new NumberNode(t.value);
        } else if (t.type == TokenType.UNARYMINUS) {
            Token u = lexer.go();
            if (u.type == TokenType.NUMBER)
                return new NumberNode(-u.value);
            else if (u.type == TokenType.LBRACKET) {
                ASTNode a = evalADD();
                lexer.go(); // RBRACKET
                return new OpNode('*', new NumberNode(-1.0), a);
            }
        } else if (t.type == TokenType.LBRACKET) {
            ASTNode a = evalADD();
            lexer.go();  // RBRACKET
            return a;
        }
        return new NumberNode(t.value);
    }

    public double evaluate(String expression) {
        lexer = new Lexer(expression);
        ASTNode ast = evalADD();
        return ast.evaluate();
    }
}

//----------------------------------------------------------------------------------------------------------------------
// Main class to test all the evaluators constructed above (all shall calculate same results)
//----------------------------------------------------------------------------------------------------------------------

public class Main {
    private static final double ACCURACY = 0.000001;

    private static int evalTest(String expression, double expected, Evaluator[] evaluators) {
        int i = 0, errCnt = 0;
        double[] results = new double[evaluators.length];

        System.out.printf("%s=%f", expression, expected);

        for (Evaluator e : evaluators) {
            double r = e.evaluate(expression);
            results[i++] = r;
            System.out.printf("=%f", r);
        }
        System.out.println();

        for (double r : results) {
            if (Math.abs(r - expected) >= ACCURACY) {
                ++errCnt;
                System.out.print("ERROR ");
            } else
                System.out.print("OK ");
        }
        System.out.println();

        return errCnt;
    }


    public static void main(String[] args) {
        int errCnt = 0;

        Evaluator[] evaluators = { new StackEvaluator(), new GrammarEvaluator(), new TreeEvaluator() };

        errCnt += evalTest("-(3 + 6)", -9.0, evaluators);
        errCnt += evalTest("1.02 + -9.02 + (1.2 * 4.034) * -2 * -4.4 + -(3 + 6)", 25.59904, evaluators);
        errCnt += evalTest("3 + 6 * 2", 15.0, evaluators);
        errCnt += evalTest("3 + 6 * -2", -9.0, evaluators);
        errCnt += evalTest("5+5+5+5+5*4", 40.0, evaluators);
        errCnt += evalTest("5+5+5*4+5+5", 40.0, evaluators);
        errCnt += evalTest("4*5+5+5+5+5", 40.0, evaluators);
        errCnt += evalTest("(5+5+5+5+5)*4", 100.0, evaluators);
        errCnt += evalTest("(5+5+5+5+5)*-4", -100.0, evaluators);
        errCnt += evalTest("-4*(5+5+5+5+5)", -100.0, evaluators);
        errCnt += evalTest("4*-(5+5+5+5+5)", -100.0, evaluators);
        errCnt += evalTest("4*-(5+5+2*5+5)", -100.0, evaluators);
        errCnt += evalTest("4*-(5+5+(2*5)+5)", -100.0, evaluators);
        errCnt += evalTest("4*-(((5+5)+(2*5))+5)", -100.0, evaluators);
        errCnt += evalTest("3*2^2*3", 36.0, evaluators);

        System.out.println();

        if (errCnt == 0)
            System.out.println("OK(" + errCnt + ")");
        else
            System.out.println("ERROR(" + errCnt + ")");
    }
}
