package com.yatracker.js;

import java.math.BigInteger;

/**
 * @author ya
 */
public final class AssertUtil {

    public static boolean equals(Number[] numbers1, Number[] numbers2) {
        if (numbers1.length != numbers2.length) {
            return false;
        }

        for (int i = 0; i < numbers1.length; i++) {
            if (!equals(numbers1[i], numbers2[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Number numbers1, Number numbers2) {
        BigInteger b1;
        if (numbers1 instanceof BigInteger) {
            b1 = (BigInteger) numbers1;
        } else {
            b1 = BigInteger.valueOf(numbers1.longValue());
        }

        BigInteger b2;
        if (numbers2 instanceof BigInteger) {
            b2 = (BigInteger) numbers2;
        } else {
            b2 = BigInteger.valueOf(numbers2.longValue());
        }

        return b1.compareTo(b2) == 0;
    }

}
