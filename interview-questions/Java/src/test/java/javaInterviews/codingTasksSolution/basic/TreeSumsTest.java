package javaInterviews.codingTasksSolution.basic;

import org.junit.jupiter.api.Test;

import javaInterviews.codingTasksSolution.basic.TreeSums.Node;
import static javaInterviews.codingTasksSolution.basic.TreeSums.sumAll;
import static javaInterviews.codingTasksSolution.basic.TreeSums.sumLeaves;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeSumsTest {

    @Test
    public void emptyTreeTest() {
        assertEquals(sumAll(null), 0);
        assertEquals(sumLeaves(null), 0);
    }

    @Test
    public void singleNodeTreeTest() {
        Node tree = new Node(123, null, null);
        assertEquals(sumAll(tree), 123);
        assertEquals(sumLeaves(tree), 123);
    }

    @Test
    public void largeTreeTest() {
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
        assertEquals(sumAll(tree), 81);
        assertEquals(sumLeaves(tree), 42);

    }
}