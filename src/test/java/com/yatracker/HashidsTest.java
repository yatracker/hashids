package com.yatracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author ya
 */
class HashidsTest {

    Hashids hashids = new HashidsBuilder()
            .salt("汉语niieani/hashids.js")
            .seps("fuck")
            .minLength(12)
            .alphabet("🍭😚中国ㅛㅜabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
            .build();

    long count = 2000L;
    List<Long> base1 = Arrays.asList(
            Long.MAX_VALUE >> 63, Long.MAX_VALUE >> 62, Long.MAX_VALUE >> 61, Long.MAX_VALUE >> 60,
            Long.MAX_VALUE >> 56, Long.MAX_VALUE >> 52, Long.MAX_VALUE >> 48, Long.MAX_VALUE >> 44,
            Long.MAX_VALUE >> 40, Long.MAX_VALUE >> 36, Long.MAX_VALUE >> 32, Long.MAX_VALUE >> 28,
            Long.MAX_VALUE >> 24, Long.MAX_VALUE >> 20, Long.MAX_VALUE >> 16, Long.MAX_VALUE >> 12,
            Long.MAX_VALUE >> 8, Long.MAX_VALUE >> 4, Long.MAX_VALUE >> 3, Long.MAX_VALUE >> 2,
            Long.MAX_VALUE >> 1, Long.MAX_VALUE - count,
            Long.MAX_VALUE - Integer.MAX_VALUE, (long) Integer.MAX_VALUE
    );

    List<BigInteger> base2 = Arrays.asList(
            BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE),
            BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(Integer.MAX_VALUE)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 62)).add(BigInteger.ONE),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 61)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 60)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 56)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 52)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 48)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 44)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 40)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 36)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 32)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 28)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 24)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 20)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 16)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 12)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 8)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 4)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 3)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 2)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE >> 1)),
            BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE))
    );

    @Test
    void one_long_number() {
        base1.forEach(base -> {
            LongStream.rangeClosed(0, count).forEach(add -> {
                long number = base + add;
                String id = hashids.encode(number);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                Number[] decoded = hashids.decode(id);
                Assertions.assertEquals(1, decoded.length);
                Number decodedNumber = decoded[0];
                Assertions.assertTrue(decodedNumber instanceof Long);
                Assertions.assertEquals(decodedNumber, number);
            });
        });
    }

    @Test
    void one_long_number_hex() {
        base1.forEach(base -> {
            LongStream.rangeClosed(0, count).forEach(add -> {
                String numberHex = Long.toString(base + add, 16);
                String id = hashids.encodeHex(numberHex);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                String decoded = hashids.decodeHex(id);
                Assertions.assertEquals(decoded, numberHex);
            });
        });
    }

    @Test
    void one_biginteger_in_long_max_value() {
        base1.forEach(base -> {
            LongStream.rangeClosed(0, count).forEach(add -> {
                BigInteger number = BigInteger.valueOf(base).add(BigInteger.valueOf(add));
                String id = hashids.encode(number);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                Number[] decoded = hashids.decode(id);
                Assertions.assertEquals(1, decoded.length);
                Number decodedNumber = decoded[0];
                Assertions.assertTrue(decodedNumber instanceof Long);
                Assertions.assertEquals(decodedNumber, number.longValue());
            });
        });
    }

    @Test
    void one_biginteger_in_long_max_value_hex() {
        base1.forEach(base -> {
            LongStream.rangeClosed(0, count).forEach(add -> {
                BigInteger number = BigInteger.valueOf(base).add(BigInteger.valueOf(add));
                String numberHex = number.toString(16);
                String id = hashids.encodeHex(numberHex);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                String decoded = hashids.decodeHex(id);
                Assertions.assertEquals(decoded, numberHex);
            });
        });
    }

    @Test
    void one_biginteger_out_long_max_value() {
        base2.forEach(base -> {
            LongStream.rangeClosed(0, count).forEach(add -> {
                BigInteger number = base.add(BigInteger.valueOf(add));
                String id = hashids.encode(number);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                Number[] decoded = hashids.decode(id);
                Assertions.assertEquals(1, decoded.length);
                Number decodedNumber = decoded[0];
                Assertions.assertTrue(decodedNumber instanceof BigInteger);
                Assertions.assertEquals(decodedNumber, number);
            });
        });
    }

    @Test
    void one_biginteger_out_long_max_value_hex() {
        base2.forEach(base -> {
            LongStream.rangeClosed(0, count).forEach(add -> {
                BigInteger number = base.add(BigInteger.valueOf(add));
                String numberHex = number.toString(16);
                String id = hashids.encodeHex(numberHex);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                String decoded = hashids.decodeHex(id);
                Assertions.assertEquals(decoded, numberHex);
            });
        });
    }

    @Test
    void long_number() {
        for (int i = 0, j = base1.size() - 1; i < j; i++, j--) {
            long n1 = base1.get(i);
            long n2 = base1.get(j);

            LongStream.rangeClosed(0L, count - 2).forEach(add -> {
                Number[] numbers = new Number[4];
                numbers[0] = n1 + add;
                numbers[1] = n2 + add;
                numbers[2] = n1 + 2 + add;
                numbers[3] = n2 + 2 + add;
                String id = hashids.encode(numbers);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                Number[] decoded = hashids.decode(id);
                Assertions.assertEquals(numbers.length, decoded.length);

                for (int a = 0; a < decoded.length; a++) {
                    Number decodedNumber = decoded[a];
                    Assertions.assertTrue(decodedNumber instanceof Long);
                    Assertions.assertEquals(decodedNumber, numbers[a]);
                }
            });
        }
    }

    @Test
    void long_number_hex() {
        for (int i = 0, j = base1.size() - 1; i < j; i++, j--) {
            long n1 = base1.get(i);
            long n2 = base1.get(j);

            LongStream.rangeClosed(0L, count - 2).forEach(add -> {
                Number[] numbers = new Number[4];
                numbers[0] = n1 + add;
                numbers[1] = n2 + add;
                numbers[2] = n1 + 2 + add;
                numbers[3] = n2 + 2 + add;
                String numberHex = Arrays.stream(numbers)
                        .map(e -> Long.toString(e.longValue(), 16))
                        .collect(Collectors.joining());
                String id = hashids.encodeHex(numberHex);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                String decoded = hashids.decodeHex(id);
                Assertions.assertEquals(numberHex, decoded);
            });
        }
    }

    @Test
    void biginteger_number() {
        for (int i = 0, j = base2.size() - 1; i < j; i++, j--) {
            BigInteger n1 = base2.get(i);
            BigInteger n2 = base2.get(j);

            LongStream.rangeClosed(0L, count).forEach(add -> {
                Number[] numbers = new Number[4];
                numbers[0] = n1.add(BigInteger.valueOf(add));
                numbers[1] = n2.add(BigInteger.valueOf(add));
                numbers[2] = n1.add(BigInteger.valueOf(add + 2));
                numbers[3] = n2.add(BigInteger.valueOf(add + 2));
                String id = hashids.encode(numbers);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                Number[] decoded = hashids.decode(id);
                Assertions.assertEquals(numbers.length, decoded.length);

                for (int a = 0; a < decoded.length; a++) {
                    Number decodedNumber = decoded[a];
                    Assertions.assertTrue(decodedNumber instanceof BigInteger);
                    Assertions.assertEquals(decodedNumber, numbers[a]);
                }
            });
        }
    }

    @Test
    void biginteger_number_hex() {
        for (int i = 0, j = base2.size() - 1; i < j; i++, j--) {
            BigInteger n1 = base2.get(i);
            BigInteger n2 = base2.get(j);

            LongStream.rangeClosed(0L, count).forEach(add -> {
                Number[] numbers = new Number[4];
                numbers[0] = n1.add(BigInteger.valueOf(add));
                numbers[1] = n2.add(BigInteger.valueOf(add));
                numbers[2] = n1.add(BigInteger.valueOf(add + 2));
                numbers[3] = n2.add(BigInteger.valueOf(add + 2));
                String numberHex = Arrays.stream(numbers)
                        .map(e -> ((BigInteger) e).toString(16))
                        .collect(Collectors.joining());
                String id = hashids.encodeHex(numberHex);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                String decoded = hashids.decodeHex(id);
                Assertions.assertEquals(decoded, numberHex);
            });
        }
    }

    @Test
    void shuffle_number() {
        for (int i = 0, j = 0; i < base1.size() && j < base2.size(); i++, j++) {
            long n1 = base1.get(i);
            BigInteger n2 = base2.get(j);

            LongStream.rangeClosed(0L, count - 2).forEach(add -> {
                Number[] numbers = new Number[4];
                numbers[0] = n1 + add;
                numbers[1] = n2.add(BigInteger.valueOf(add));
                numbers[2] = n1 + add + 2;
                numbers[3] = n2.add(BigInteger.valueOf(add + 2));
                String id = hashids.encode(numbers);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                Number[] decoded = hashids.decode(id);
                Assertions.assertEquals(numbers.length, decoded.length);

                for (int a = 0; a < decoded.length; a++) {
                    Number decodedNumber = decoded[a];
                    if (a % 2 == 0) {
                        Assertions.assertTrue(decodedNumber instanceof Long);
                    } else {
                        Assertions.assertTrue(decodedNumber instanceof BigInteger);
                    }
                    Assertions.assertEquals(decodedNumber, numbers[a]);
                }
            });
        }
    }

    @Test
    void shuffle_number_hex() {
        for (int i = 0, j = 0; i < base1.size() && j < base2.size(); i++, j++) {
            long n1 = base1.get(i);
            BigInteger n2 = base2.get(j);

            LongStream.rangeClosed(0L, count - 2).forEach(add -> {
                Number[] numbers = new Number[4];
                numbers[0] = n1 + add;
                numbers[1] = n2.add(BigInteger.valueOf(add));
                numbers[2] = n1 + add + 2;
                numbers[3] = n2.add(BigInteger.valueOf(add + 2));

                String numberHex = Arrays.stream(numbers)
                        .map(e -> {
                            if (e instanceof BigInteger) {
                                return ((BigInteger) e).toString(16);
                            } else {
                                return Long.toString(e.longValue(), 16);
                            }
                        })
                        .collect(Collectors.joining());

                String id = hashids.encodeHex(numberHex);
                Assertions.assertNotEquals("", id);
                Assertions.assertTrue(id.length() >= 12);

                String decoded = hashids.decodeHex(id);
                Assertions.assertEquals(numberHex, decoded);
            });
        }
    }

}