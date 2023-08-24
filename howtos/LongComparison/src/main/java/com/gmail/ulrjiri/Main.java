/*
  Sample program to demonstrate bad and good usage of equals and hashcode.
  Philosophy: One person can be just once in the barber at one moment in time. It cannot appear twice.
 */
package com.gmail.ulrjiri;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Main {

    public static void main(String[] args) {

        Long a, b;

        // NOTE:
        // 1. Java has constant pool for values <-128;127> => the objects will be the same
        // 2. Operator "==" will compare whether the "a" and "b" are references to the same object!

        // they both will refer to the same object in the constant pool
        a = 0L;
        b = 0L;
        assertTrue(a == b);

        // explicitly craetong two new object outside of the constant pool (currently deprecated)
        a = new Long(0L);
        b = new Long(0L);
        // they will not refer to the ame object
        assertFalse(a == b);
        // but the referenced objects will be equal
        assertTrue(a.equals(b));
        // their outboxed values will be also equal
        assertTrue((long) a == (long) b);
        assertTrue(a.longValue() == b.longValue());

        assertTrue(a == (long) b);
        assertTrue((long) a == b);

        // under or equal 127 -> will refer to the same object in the constant pool -> they will be equal
        a = Long.valueOf(0);
        b = Long.valueOf(0);
        assertTrue(a == b);
        a = Long.valueOf(1);
        b = Long.valueOf(1);
        assertTrue(a == b);
        a = Long.valueOf(127);
        b = Long.valueOf(127);
        assertTrue(a == b);
        // over 127 -> new objects will be created outside the constant pool -> they will be different
        a = Long.valueOf(128);
        b = Long.valueOf(128);
        assertFalse(a == b);
        a = Long.valueOf(1000);
        b = Long.valueOf(1000);
        assertFalse(a == b);
        // If any operand is of a reference type, it is subjected to unboxing conversion -> object gets outboxed
        assertTrue(a == (long) b);
        assertTrue((long) a == b);
    }
}
