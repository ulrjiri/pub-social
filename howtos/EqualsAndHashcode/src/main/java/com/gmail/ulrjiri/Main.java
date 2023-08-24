/*
  Sample program to demonstrate bad and good usage of equals and hashcode.
  Philosophy: One person can be just once in the barber at one moment in time. It cannot appear twice.
 */
package com.gmail.ulrjiri;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Just a helper to make testing easier
 */
interface Hairy {
    void cutHair();
}

/**
 * Bad usage
 * Equal and HashCode will use all non-static members of the class including hair length.
 * After haircut, the person becomes somebody else.
 */
@EqualsAndHashCode
@ToString
class TheBad implements Hairy {
    public int id = 1;
    public String name = "Bad";
    public int hairLength = 10;  // it is really a bad idea to include hair length to hashcode and equals

    @Override
    public void cutHair() {
        hairLength--;
    }
}

/**
 * Good usage - hair length in excluded from hashcode and equals.
 * It means that the person will still be the same independent from the length of hair.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
class TheGood implements Hairy {
    @EqualsAndHashCode.Include
    public int id = 2;
    @EqualsAndHashCode.Include
    public String name = "Good";
    public int hairLength = 20;

    @Override
    public void cutHair() {
        hairLength--;
    }
}

public class Main {

    public static void haveHaircut(Hairy hairy) {
        Set<Hairy> barberShop = new HashSet<>();

        System.out.println("Before Haircut" + hairy);
        System.out.println("  Hashcode: " + hairy.hashCode());
        System.out.println("  Come to barber shop? " + barberShop.add(hairy));
        System.out.println("  In the barber shop? " + barberShop.contains(hairy));
        System.out.println("This is me in the barber shop right now:" + barberShop);

        System.out.println("Have haircut");
        hairy.cutHair();

        System.out.println("  Hashcode: " + hairy.hashCode());
        System.out.println("  Still in the barber shop? " + barberShop.contains(hairy));
        System.out.println("  Come again to he barber shop? " + barberShop.add(hairy));
        System.out.println("  Still in the barber shop? " + barberShop.contains(hairy));
        System.out.println("This is me in the barber shop right now:" + barberShop);
    }

    public static void main(String[] args) {
        haveHaircut(new TheBad());
        System.out.println("----");
        haveHaircut(new TheGood());
    }
}