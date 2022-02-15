package javaInterviews.codingTasks.basic;

public class TreeSum {

    public static class Node {
        int value;
        Node left, right;

        public Node(int value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    // COMPLETE THIS
    public static int sumAll(Node root) {
        return 0;
    }

    // COMPLETE THIS
    public static int sumLeaves(Node root) {
        return 0;
    }

    public static void main(String[] argv) {

        System.out.println("Tree walker");
        System.out.println("===========");

        Node tree = new Node(5,
                new TreeSum.Node(6,
                        new TreeSum.Node(8, null, null),
                        new TreeSum.Node(9,
                                new TreeSum.Node(10, null, null),
                                new TreeSum.Node(11, null, null))),
                new TreeSum.Node(7,
                        null,
                        new TreeSum.Node(12,
                                null,
                                new TreeSum.Node(13, null, null))));

        int sumAll = sumAll(tree);
        System.out.println(sumAll);  // 81 expected

        int sumLeaves = sumLeaves(tree);
        System.out.println(sumLeaves);  // 42 expected
    }
}
