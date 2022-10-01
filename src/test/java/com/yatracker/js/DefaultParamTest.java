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
class DefaultParamTest {
    
    Hashids hashids = new HashidsBuilder().build();

    private static final List<Pair> pairs = Arrays.asList(
            new Pair("gY", new Number[] {0}),
            new Pair("jR", new Number[] {1}),
            new Pair("R8ZN0", new Number[] {928_728}),
            new Pair("o2fXhV", new Number[] {1, 2, 3}),
            new Pair("jRfMcP", new Number[] {1, 0, 0}),
            new Pair("jQcMcW", new Number[] {0, 0, 1}),
            new Pair("gYcxcr", new Number[] {0, 0, 0}),
            new Pair("gLpmopgO6", new Number[] {1_000_000_000_000L}),
            new Pair("lEW77X7g527", new Number[] {9_007_199_254_740_991L}),
            new Pair("BrtltWt2tyt1tvt7tJt2t1tD", new Number[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}),
            new Pair("G6XOnGQgIpcVcXcqZ4B8Q8B9y", new Number[] {10_000_000_000L, 0, 0, 0, 999_999_999_999_999L}),
            new Pair("5KoLLVL49RLhYkppOplM6piwWNNANny8N", new Number[] {9_007_199_254_740_991L, 9_007_199_254_740_991L, 9_007_199_254_740_991L}),
            new Pair("BPg3Qx5f8VrvQkS16wpmwIgj9Q4Jsr93gqx", new Number[] {1_000_000_001, 1_000_000_002, 1_000_000_003, 1_000_000_004, 1_000_000_005}),
            new Pair("1wfphpilsMtNumCRFRHXIDSqT2UPcWf1hZi3s7tN", new Number[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
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
