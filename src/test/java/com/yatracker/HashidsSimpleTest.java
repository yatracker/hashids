package com.yatracker;

import com.yatracker.js.AssertUtil;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ya
 */
class HashidsSimpleTest {

    Hashids hashids1 = new HashidsBuilder().build();

    Hashids hashids2 = new HashidsBuilder().salt("ab12").minLength(12).build();

    void test(Number ...numbers) {
        test0(numbers);
        List<Number> list = Arrays.asList(numbers);
        Collections.reverse(list);
        test0(list.toArray(new Number[0]));
    }

    void test0(Number ...numbers) {
        String encoded = hashids1.encode(numbers);
        Number[] decoded = hashids1.decode(encoded);
        AssertUtil.equals(numbers, decoded);

        encoded = hashids2.encode(numbers);
        decoded = hashids2.decode(encoded);
        AssertUtil.equals(numbers, decoded);
    }

    @Test
    void test() {
        test(Long.MAX_VALUE);
        test(Long.MAX_VALUE, Long.MAX_VALUE - 1);
        test(Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MAX_VALUE - 2);
        test(Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MAX_VALUE - 2, Long.MAX_VALUE - 3);
        test(Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MAX_VALUE - 2, Long.MAX_VALUE - 3, Long.MAX_VALUE - 4);
        System.out.println();

        int len = String.valueOf(Long.MAX_VALUE).length();
        for (int i = 0; i <= len; i++) {
            Number[] numbers = new Number[i + 1];
            for (int j = 0; j <= i; j++) {
                numbers[j] = (long) (Long.MAX_VALUE / (Math.pow(10, (len - j))));
            }
            test(numbers);
        }

        System.out.println();

        for (int i = 0; i < 32; i++) {
            Number[] numbers = new Number[i + 1];
            for (int j = 0; j <= i; j++) {
                numbers[j] = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(1L << j));
            }
            test(numbers);
        }
        System.out.println();
    }

}