package edu.iate.ism22.schedule.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

class CircularLinkedListTest {
    
    @BeforeEach
    void setUp() {
    }
    
    @Test
    void getList() {
        Random rand = new Random();
        
        CircularLinkedList<String> circ = new CircularLinkedList<>(rand);
        circ.add("first");
        circ.add("second");
        circ.add("third");
        circ.add("forth");
        circ.add("fifth");
        
        List<String> result = circ.getList();
        System.out.println(result);
    }
}