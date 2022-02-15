package javaInterviews.codingTasks.basic;

public class Triangle {

    // COMPLETE THIS
    public static String printTriangle(int n, char c) {
        return null;
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
