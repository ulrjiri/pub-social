package javaInterviews.codingTasksSolution.basic;

import org.junit.jupiter.api.Test;
import static javaInterviews.codingTasksSolution.basic.Arrays.reverse;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArraysTest {

    @Test
    public void reverseTest() {
        assertNull(reverse(null));
        assertEquals(reverse(new int[] {}).length, 0);
        assertArrayEquals(reverse(new int[] {}), new int[] {});
        assertArrayEquals(reverse(new int[] { 123 }), new int[] { 123 });
        assertArrayEquals(reverse(new int[] { 123, 456 }), new int[] { 456, 123 });
        assertArrayEquals(reverse(new int[] { 1, 2, 3, 4 }), new int[] { 4, 3, 2, 1 });
        assertArrayEquals(reverse(new int[] { 1, 2, 3, 4, 5, 6, 7 }), new int[] { 7, 6, 5, 4, 3, 2, 1 });

        int[] a = { 1, 2, 3};
        int[] b = reverse(a);
        assertArrayEquals(a, new int[] { 1, 2, 3 });
        assertArrayEquals(b, new int[] { 3, 2, 1 });
    }
}