package javaInterviews.codingTasksSolution.basic;

public class TreeSums {

    public static class Node {
        int value;
        Node left, right;

        public Node(int value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    public static int sumAll(Node root) {
        return root == null ? 0 : (root.value + sumAll(root.left) + sumAll(root.right));
    }

    public static int sumLeaves(Node root) {
        if (root != null) {
            if (root.left == null && root.right == null) return root.value;
            return sumLeaves(root.left) + sumLeaves(root.right);
        }
        return 0;
    }

    public static void main(String[] argv) {

        System.out.println("Tree walker");
        System.out.println("===========");

        Node tree = new Node(5,
                new Node(6,
                        new Node(8, null, null),
                        new Node(9,
                                new Node(10, null, null),
                                new Node(11, null, null))),
                new Node(7,
                        null,
                        new Node(12,
                                null,
                                new Node(13, null, null))));

        int sumAll = sumAll(tree);
        System.out.println(sumAll);  // 81 expected

        int sumLeaves = sumLeaves(tree);
        System.out.println(sumLeaves);  // 42 expected
    }
}
