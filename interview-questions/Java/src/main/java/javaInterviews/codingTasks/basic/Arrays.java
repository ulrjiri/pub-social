package javaInterviews.codingTasks.basic;

public class Arrays {

    // COMPLETE THIS
    public static int[] reverse(int[] a) {
        return null;
    }

    public static void main(String[] argv) {

        System.out.println("Array reverse");
        System.out.println("=============");

        int[] a = {1, 2, 3, 4};
        int[] b = reverse(a);
        System.out.println(java.util.Arrays.toString(a));
        // [1, 2, 3, 4] expected
        System.out.println(java.util.Arrays.toString(b));
        // [4, 3, 2, 1] expected

        int[] c = {1, 2, 3, 4, 5, 6, 7};
        int[] d = reverse(c);
        System.out.println(java.util.Arrays.toString(c));
        // [1, 2, 3, 4, 5, 6, 7] expected
        System.out.println(java.util.Arrays.toString(d));
        // [7, 6, 5, 4, 3, 2, 1] expected
    }
}
