package com.yatracker.js;

import com.yatracker.Hashids;
import com.yatracker.HashidsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ya
 */
class MiniLengthTest {

    private static void testMinLength(int minLength) {
        Hashids hashids = new HashidsBuilder().minLength(minLength).build();
        Number[] numbers = new Number[] {1, 2, 3};
        String id = hashids.encode(numbers);
        Number[] decodedNumbers = hashids.decode(id);
        AssertUtil.equals(numbers, decodedNumbers);
        Assertions.assertTrue(id.length() >= minLength);
    }

    @Test
    void test() {
        testMinLength(0);
        testMinLength(1);
        testMinLength(10);
        testMinLength(999);
        testMinLength(1000);
    }

}
