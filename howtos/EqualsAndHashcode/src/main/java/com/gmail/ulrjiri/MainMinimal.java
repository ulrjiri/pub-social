package com.gmail.ulrjiri;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@ToString
class Item {
    public int itemId = 1;
    public boolean isSold = false;
}

public class MainMinimal {
    public static void main(String[] args) {
        Set<Item> set = new HashSet<>();

        Item item = new Item();
        System.out.println("Hashcode of " + item + " is " + item.hashCode());
        set.add(item);
        System.out.println("Is " + item + " in set? " + set.contains(item));

        item.isSold = true;
        System.out.println("Is " + item + " still in set? " + set.contains(item));
        System.out.println("Hashcode of " + item + " is " + item.hashCode());
        set.add(item);
        System.out.println("Is " + item + " now in set? " + set.contains(item));

        System.out.println("What is in the set now: " + set);
    }
}