package com.es.elasticsearch.util;

import java.util.Random;

/**
 * @author songning
 * @date 2019/9/29
 * description
 */
public class RandomUtil {
    public static String getRandom(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return String.valueOf(randomNum);
    }
}
