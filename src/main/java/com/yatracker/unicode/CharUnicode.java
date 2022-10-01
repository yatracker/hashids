package com.yatracker.unicode;

import lombok.RequiredArgsConstructor;

/**
 * @author ya
 */
@RequiredArgsConstructor
public class CharUnicode implements Unicode {

    private final char ch;

    @Override
    public int codePoint() {
        return ch;
    }

    @Override
    public char[] toCharArray() {
        return new char[] {ch};
    }

    @Override
    public int hashCode() {
        return ch;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CharUnicode)) {
            return false;
        }
        return ch == ((CharUnicode) other).ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }
}
