package uk.me.mattgill.samples.richfaces;

import java.util.Random;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RandomNumberGenerator {

    private final Random random = new Random();

    public int getNumberBetween(int low, int high) {
        return random.nextInt(high - low + 1) + low;
    }

}