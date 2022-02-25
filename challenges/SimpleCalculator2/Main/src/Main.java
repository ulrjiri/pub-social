enum TokenType {
    NUMBER, OPERATOR_ADD, OPERATOR_MUL
}

class Token {
    TokenType type;
    int value;
    char operator;

    public Token(TokenType type, int value, char operator) {
        this.type = type;
        this.value = value;
        this.operator = operator;
    }
}

class Lexer {
    private int index;
    private final char[] array;
    private Token lastToken = null, nextToken = null;

    public Lexer(String expr) {
        array = expr.toCharArray();
        index = 0;
    }

    private boolean isNextNumeric() {
        return index < array.length && array[index] >= '0' && array[index] <= '9';
    }

    private boolean isNextOperatorAdd() {
        return index < array.length && (array[index] == '+' || array[index] == '-');
    }

    private boolean isNextOperatorMul() {
        return index < array.length && (array[index] == '*' || array[index] == '/');
    }

    private void ignoreWhiteSpaces() {
        while (index < array.length && (array[index] == ' ' || array[index] == '\t'))
            ++index;
    }

    private int readNumber() {
        int tmp = array[index] - '0';
        ++index;
        while (isNextNumeric())
            tmp = (tmp * 10) + array[index++] - '0';
        return tmp;
    }

    Token go() {
        if (nextToken != null) {
            lastToken = nextToken;
            nextToken = null;
            return lastToken;
        }

        if (index == array.length) {
            lastToken = null;
            return null;
        }

        ignoreWhiteSpaces();

        if (isNextNumeric())
            lastToken = new Token(TokenType.NUMBER, readNumber(), '0');
        else if (isNextOperatorAdd())
            lastToken = new Token(TokenType.OPERATOR_ADD, -1, array[index++]);
        else if (isNextOperatorMul())
            lastToken = new Token(TokenType.OPERATOR_MUL, -1, array[index++]);

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

class Interpreter {
    private Lexer lexer;

    private int evalADD() {
        int mul = evalMUL();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR_ADD) {
            lexer.go();
            return evalADD_RES(mul, t.operator);
        } else {
            return mul;
        }
    }

    private int evalADD_RES(int left, char operator) {
        int r = left;
        if (operator == '+')
            r = left + evalMUL();
        else if (operator == '-')
            r = left - evalMUL();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR_ADD) {
            lexer.go();
            return evalADD_RES(r, t.operator);
        } else
            return r;
    }

    private int evalMUL() {
        int ele = evalELE();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR_MUL) {
            lexer.go();
            return evalMUL_RES(ele, t.operator);
        } else {
            return ele;
        }
    }

    private int evalMUL_RES(int left, char operator) {
        int r = left;
        if (operator == '*')
            r = left * evalMUL();
        else if (operator == '/')
            r = left / evalMUL();
        Token t = lexer.show();
        if (t != null && t.type == TokenType.OPERATOR_MUL) {
            lexer.go();
            return evalMUL_RES(r, t.operator);
        } else
            return r;
    }

    private int evalELE() {
        return lexer.go().value;
    }

    public int evaluate(String expression) {
        lexer = new Lexer(expression);
        return evalADD();
    }
}


public class Main {

    private static void evalTest(String expression, int expected) {
        int r = new Interpreter().evaluate(expression);
        System.out.printf("%s=%d=%d %s\n", expression, expected, r, (r == expected ? "OK" : "ERROR"));

    }

    public static void main(String[] args) {
        evalTest("369", 369);
        evalTest("3 + 6", 9);
        evalTest("1+2+3", 6);
        evalTest("1000+200+30+4", 1234);
        evalTest("1000+200+30+4-1000+1000", 1234);
        evalTest("1*2*3", 6);
        evalTest("1 -9 + 2*4 -2-4 + 3 + 3", 0);
        evalTest("3 + 6 * 2", 15);
        evalTest("3 * 6 * 2", 36);
    }
}
