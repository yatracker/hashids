package com.yatracker;

import com.yatracker.unicode.CharUnicode;
import com.yatracker.unicode.DualCharUnicode;
import com.yatracker.unicode.Unicode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ya
 */
class UnicodeArrayBuilderTest {

    static UnicodeArrayBuilder push() {
        UnicodeArrayBuilder builder = new UnicodeArrayBuilder();
        builder.push(new CharUnicode('a'));
        Assertions.assertEquals(1, builder.size());
        Assertions.assertTrue(builder.capacity() >= builder.size());
        Assertions.assertEquals(new CharUnicode('a'), builder.alphabetAt(0));

        Unicode[] array = new Unicode[] {new CharUnicode('b')};
        builder.push(array);
        Assertions.assertEquals(2, builder.size());
        Assertions.assertTrue(builder.capacity() >= builder.size());
        Assertions.assertEquals(new CharUnicode('a'), builder.alphabetAt(0));
        Assertions.assertEquals(new CharUnicode('b'), builder.alphabetAt(1));

        array = new Unicode[] {new CharUnicode('c'), new DualCharUnicode("😀".toCharArray())};
        builder.push(array);
        Assertions.assertEquals(4, builder.size());
        Assertions.assertTrue(builder.capacity() >= builder.size());
        Assertions.assertEquals(new CharUnicode('a'), builder.alphabetAt(0));
        Assertions.assertEquals(new CharUnicode('b'), builder.alphabetAt(1));
        Assertions.assertEquals(new CharUnicode('c'), builder.alphabetAt(2));
        Assertions.assertEquals(new DualCharUnicode("😀".toCharArray()), builder.alphabetAt(3));
        return builder;
    }

    static UnicodeArrayBuilder unshift(UnicodeArrayBuilder builder) {
        // region test unshift
        builder.unshift(new CharUnicode('9'));
        Assertions.assertEquals(5, builder.size());
        Assertions.assertTrue(builder.capacity() >= builder.size());
        Assertions.assertEquals(new CharUnicode('9'), builder.alphabetAt(0));
        Assertions.assertEquals(new CharUnicode('a'), builder.alphabetAt(1));
        Assertions.assertEquals(new CharUnicode('b'), builder.alphabetAt(2));
        Assertions.assertEquals(new CharUnicode('c'), builder.alphabetAt(3));
        Assertions.assertEquals(new DualCharUnicode("😀".toCharArray()), builder.alphabetAt(4));

        Unicode[] array = new Unicode[] {new CharUnicode('8')};
        builder.unshift(array);
        Assertions.assertEquals(6, builder.size());
        Assertions.assertTrue(builder.capacity() >= builder.size());
        Assertions.assertEquals(new CharUnicode('8'), builder.alphabetAt(0));
        Assertions.assertEquals(new CharUnicode('9'), builder.alphabetAt(1));
        Assertions.assertEquals(new CharUnicode('a'), builder.alphabetAt(2));
        Assertions.assertEquals(new CharUnicode('b'), builder.alphabetAt(3));
        Assertions.assertEquals(new CharUnicode('c'), builder.alphabetAt(4));
        Assertions.assertEquals(new DualCharUnicode("😀".toCharArray()), builder.alphabetAt(5));

        array = new Unicode[] {new CharUnicode('7'), new DualCharUnicode("🍭".toCharArray())};
        builder.unshift(array);
        Assertions.assertEquals(8, builder.size());
        Assertions.assertTrue(builder.capacity() >= builder.size());
        Assertions.assertEquals(new CharUnicode('7'), builder.alphabetAt(0));
        Assertions.assertEquals(new DualCharUnicode("🍭".toCharArray()), builder.alphabetAt(1));
        Assertions.assertEquals(new CharUnicode('8'), builder.alphabetAt(2));
        Assertions.assertEquals(new CharUnicode('9'), builder.alphabetAt(3));
        Assertions.assertEquals(new CharUnicode('a'), builder.alphabetAt(4));
        Assertions.assertEquals(new CharUnicode('b'), builder.alphabetAt(5));
        Assertions.assertEquals(new CharUnicode('c'), builder.alphabetAt(6));
        Assertions.assertEquals(new DualCharUnicode("😀".toCharArray()), builder.alphabetAt(7));
        return builder;
    }

    @Test
    void test() {
        UnicodeArrayBuilder builder = builder = unshift(push());

        builder = builder.slice(0, builder.size());
        Assertions.assertEquals("7\uD83C\uDF6D89abc\uD83D\uDE00", builder.toString());
        builder = builder.slice(4, 7);
        Assertions.assertEquals("abc", builder.toString());

        builder = new UnicodeArrayBuilder();
        builder.push(UnicodeArrayUtil.from("abcde"));
        builder.unshift(UnicodeArrayUtil.from("12345"));
        builder.unshift(UnicodeArrayUtil.from("ABCDE"));
        System.out.println(builder.capacity());
    }
}