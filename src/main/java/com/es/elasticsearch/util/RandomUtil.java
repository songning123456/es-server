package com.es.elasticsearch.util;

import java.util.Random;

/**
 * @author songning
 * @date 2019/9/29
 * description
 */
public class RandomUtil {
    public static String getRandom(int min, int max) {
        Random random = new Random();
        return String.valueOf(random.nextInt(max) % (max - min + 1) + min);
    }
}
