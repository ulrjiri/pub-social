package javaInterviews.codingTasksSolution.basic;

public class Triangles {

    public static String printTriangle(int n, char c) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++)
                s.append(c);
            s.append('\n');
        }
        return s.toString();
    }

    public static void main(String[] argv) {

        System.out.println("Triangle");
        System.out.println("========");

        String triangle = printTriangle(1, '#');
        System.out.print(triangle);
        // #
        // expected

        triangle = printTriangle(5, '*');
        System.out.print(triangle);
        // *
        // **
        // ***
        // ****
        // *****
        // expected
    }
}
