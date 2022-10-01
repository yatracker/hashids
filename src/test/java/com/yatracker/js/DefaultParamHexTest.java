package com.yatracker.js;

import com.yatracker.Hashids;
import com.yatracker.HashidsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author ya
 */
class DefaultParamHexTest {

    Hashids hashids = new HashidsBuilder().build();

    private static final List<PairHex> pairs = Arrays.asList(
            new PairHex("wpVL4j9g", "deadbeef"),
            new PairHex("kmP69lB3xv", "abcdef123456"),
            new PairHex("47JWg0kv4VU0G2KBO2", "ABCDDD6666DDEEEEEEEEE"),
            new PairHex("y42LW46J9luq3Xq9XMly", "507f1f77bcf86cd799439011"),
            new PairHex("m1rO8xBQNquXmLvmO65BUO9KQmj", "f00000fddddddeeeee4444444ababab"),
            new PairHex("wBlnMA23NLIQDgw7XxErc2mlNyAjpw", "abcdef123456abcdef123456abcdef123456"),
            new PairHex("VwLAoD9BqlT7xn4ZnBXJFmGZ51ZqrBhqrymEyvYLIP199", "f000000000000000000000000000000000000000000000000000f"),
            new PairHex("nBrz1rYyV0C0XKNXxB54fWN0yNvVjlip7127Jo3ri0Pqw", "fffffffffffffffffffffffffffffffffffffffffffffffffffff")
    );

    @Test
    void test() {
        for (PairHex pair : pairs) {
            String id = pair.id;
            String hex = pair.hex;
            Assertions.assertEquals(id, hashids.encodeHex(hex));

            String encodedId = hashids.encodeHex(hex);
            String decodedHex = hashids.decodeHex(encodedId);
            Assertions.assertEquals(decodedHex, hex.toLowerCase(Locale.ENGLISH));
        }
    }

}
