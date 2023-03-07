package com.cloudofgoods.xenia;

import java.util.Random;

public class RandomNumberGenerator {
    public static void main(String[] args) {
        int percentage = 30; // set the percentage here
        Random random = new Random();
        double randomNumber = random.nextDouble();
        if (randomNumber < percentage/100.0) {
            // the random number falls within the percentage range
            System.out.println("Random number generated: " + randomNumber + ", which falls within " + percentage + "%");
        } else {
            System.out.println("Random number generated: " + randomNumber + ", which falls outside " + percentage + "%");
        }
    }
}