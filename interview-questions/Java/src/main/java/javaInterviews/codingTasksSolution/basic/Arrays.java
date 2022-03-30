package javaInterviews.codingTasksSolution.basic;

public class Arrays {

    public static int[] reverse(int[] a) {
        if (a == null)
            return null;
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++)
            b[i] = a[a.length - 1 - i];
        return b;
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
        // [1, 2, 3, 4, 6, 7] expected
        System.out.println(java.util.Arrays.toString(d));
        // [7, 6, 5, 4, 3, 2, 1] expected
    }
}
