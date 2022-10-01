package com.yatracker.js;

import com.yatracker.Hashids;
import com.yatracker.HashidsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ya
 */
class CustomSaltTest {

    private static void testSalt(String salt) {
        Hashids hashids = new HashidsBuilder().salt(salt).build();
        Number[] numbers = new Number[] {1, 2, 3};

        String id = hashids.encode(numbers);
        Number[] decodedNumbers = hashids.decode(id);
        AssertUtil.equals(numbers, decodedNumbers);
    }

    @Test
    void test() {
        testSalt("");
        testSalt("   ");
        testSalt("this is my salt");
        testSalt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890`~!@#$%^&*()-_=+\\|'\";:/?.>,<{[}]");
        testSalt("`~!@#$%^&*()-_=+\\|'\";:/?.>,<{[}]");
        testSalt("🤺👩🏿‍🦳🛁👩🏻🦷🤦‍♂️🐁☝🏼✍🏾👉🏽🇸🇰❤️🍭");
        testSalt("\uD83D\uDE0D\uD83E\uDDD1\uD83C\uDFFD\u200D\uD83E\uDDB3\uD83E\uDDD1\uD83C\uDF77\uD83D\uDC69\uD83C\uDFFF\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFE\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFD\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFB\u200D\uD83E\uDDB0✍\uD83C\uDFFE\uD83D\uDC49\uD83C\uDFFD\uD83D\uDC69\uD83C\uDFFB\uD83E\uDDB7\uD83E\uDD26\u200D♂️");
    }

}
