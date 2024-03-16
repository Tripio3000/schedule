package edu.iate.ism22.schedule.entity.genetic;

import org.junit.jupiter.api.Test;

import java.util.Random;

class TwoPointsCrossingTest {
    
    @Test
    void cross() {
        Random rand = new Random();
        
        int num = rand.nextInt(20) + 1;
        
        System.out.println(num);
    }
}