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
class CustomParamHexTest {

    Hashids hashids = new HashidsBuilder()
            .salt("this is my salt")
            .minLength(30)
            .alphabet("xzal86grmb4jhysfoqp3we7291kuct5iv0nd")
            .build();

    List<PairHex> pairs = Arrays.asList(
            new PairHex("0dbq3jwa8p4b3gk6gb8bv21goerm96", "deadbeef"),
            new PairHex("190obdnk4j02pajjdande7aqj628mr", "abcdef123456"),
            new PairHex("a1nvl5d9m3yo8pj1fqag8p9pqw4dyl", "ABCDDD6666DDEEEEEEEEE"),
            new PairHex("1nvlml93k3066oas3l9lr1wn1k67dy", "507f1f77bcf86cd799439011"),
            new PairHex("mgyband33ye3c6jj16yq1jayh6krqjbo", "f00000fddddddeeeee4444444ababab"),
            new PairHex("9mnwgllqg1q2tdo63yya35a9ukgl6bbn6qn8", "abcdef123456abcdef123456abcdef123456"),
            new PairHex("edjrkn9m6o69s0ewnq5lqanqsmk6loayorlohwd963r53e63xmml29", "f000000000000000000000000000000000000000000000000000f"),
            new PairHex("grekpy53r2pjxwyjkl9aw0k3t5la1b8d5r1ex9bgeqmy93eata0eq0", "fffffffffffffffffffffffffffffffffffffffffffffffffffff")
    );
    
    @Test
    void test() {
        for (PairHex pair : pairs) {
            String id = pair.id;
            String hex = pair.hex;
            Assertions.assertEquals(id, hashids.encodeHex(hex));

            Assertions.assertEquals(hex.toLowerCase(), hashids.decodeHex(id));

            String encodedId = hashids.encodeHex(hex);
            String decodedHex = hashids.decodeHex(encodedId);

            Assertions.assertEquals(decodedHex, hex.toLowerCase());

            Assertions.assertTrue(hashids.encodeHex(hex).length() >= 30);
        }
    }
}
