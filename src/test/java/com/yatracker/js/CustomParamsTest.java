package com.yatracker.js;

import com.yatracker.Hashids;
import com.yatracker.HashidsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author ya
 */
class CustomParamsTest {

    Hashids hashids = new HashidsBuilder()
            .salt("this is my salt")
            .minLength(30)
            .alphabet("xzal86grmb4jhysfoqp3we7291kuct5iv0nd")
            .build();

    private static final List<Pair> pairs = Arrays.asList(
            new Pair("nej1m3d5a6yn875e7gr9kbwpqol02q", new Number[] {0}),
            new Pair("dw1nqdp92yrajvl9v6k3gl5mb0o8ea", new Number[] {1}),
            new Pair("onqr0bk58p642wldq14djmw21ygl39", new Number[] {928_728}),
            new Pair("18apy3wlqkjvd5h1id7mn5ore2d06b", new Number[] {1, 2, 3}),
            new Pair("o60edky1ng3vl9hbfavwr5pa2q8mb9", new Number[] {1, 0, 0}),
            new Pair("o60edky1ng3vlqfbfp4wr5pa2q8mb9", new Number[] {0, 0, 1}),
            new Pair("qek2a08gpl575efrfd7yomj9dwbr63", new Number[] {0, 0, 0}),
            new Pair("m3d5a6yn875rae8y81a94gr9kbwpqo", new Number[] {1_000_000_000_000L}),
            new Pair("1q3y98ln48w96kpo0wgk314w5mak2d", new Number[] {9_007_199_254_740_991L}),
            new Pair("op7qrcdc3cgc2c0cbcrcoc5clce4d6", new Number[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}),
            new Pair("5430bd2jo0lxyfkfjfyojej5adqdy4", new Number[] {10_000_000_000L, 0, 0, 0, 999_999_999_999_999L}),
            new Pair("aa5kow86ano1pt3e1aqm239awkt9pk380w9l3q6", new Number[] {9_007_199_254_740_991L, 9_007_199_254_740_991L, 9_007_199_254_740_991L}),
            new Pair("mmmykr5nuaabgwnohmml6dakt00jmo3ainnpy2mk", new Number[] {1_000_000_001, 1_000_000_002, 1_000_000_003, 1_000_000_004, 1_000_000_005}),
            new Pair("w1hwinuwt1cbs6xwzafmhdinuotpcosrxaz0fahl", new Number[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    );
    
    @Test
    void test() {
        for (Pair pair : pairs) {
            Number[] decoded = hashids.decode(pair.id);
            AssertUtil.equals(decoded, pair.numbers);

            String encoded = hashids.encode(pair.numbers);
            Assertions.assertEquals(encoded, pair.id);
        }
    }

}
