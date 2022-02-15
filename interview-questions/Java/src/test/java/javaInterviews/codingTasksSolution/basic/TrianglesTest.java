package javaInterviews.codingTasksSolution.basic;

import org.junit.jupiter.api.Test;

import static javaInterviews.codingTasksSolution.basic.Triangles.printTriangle;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrianglesTest {

    @Test
    public void decodeTest() {
        assertEquals(printTriangle(-5, 'x'), "");
        assertEquals(printTriangle(0, 'Y'), "");
        assertEquals(printTriangle(1, '#'), "#\n");
        assertEquals(printTriangle(2, '*'), "*\n**\n");
        assertEquals(printTriangle(3, '*'), "*\n**\n***\n");
        assertEquals(printTriangle(4, '*'), "*\n**\n***\n****\n");
        assertEquals(printTriangle(5, '*'), "*\n**\n***\n****\n*****\n");
    }
}