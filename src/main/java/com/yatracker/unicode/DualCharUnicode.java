package com.yatracker.unicode;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author ya
 */
@RequiredArgsConstructor
public class DualCharUnicode implements Unicode {

    private final char[] chars;

    @Override
    public int codePoint() {
        return Character.codePointAt(chars, 0);
    }

    @Override
    public char[] toCharArray() {
        return chars;
    }

    @Override
    public int hashCode() {
        return chars[0] * 31 + chars[1] + Character.MAX_VALUE + 1;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DualCharUnicode)) {
            return false;
        }
        return Arrays.equals(chars, ((DualCharUnicode) other).chars);
    }

    @Override
    public String toString() {
        return new String(chars);
    }

}
