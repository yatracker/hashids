package com.yatracker;

import com.yatracker.unicode.CharUnicode;
import com.yatracker.unicode.DualCharUnicode;
import com.yatracker.unicode.Unicode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ya
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UnicodeArrayUtil {

    public static Unicode[] from(String source) {
        int length = source.length();
        List<Unicode> alphabets = new ArrayList<>(length);
        int i = 0;
        while (i < length) {
            int codePoint = source.codePointAt(i);
            Unicode alphabet;
            if (Character.isSupplementaryCodePoint(codePoint)) {
                alphabet = new DualCharUnicode(Character.toChars(codePoint));
                i += 2;
            } else {
                alphabet = new CharUnicode((char) codePoint);
                i++;
            }
            alphabets.add(alphabet);
        }
        return alphabets.toArray(new Unicode[0]);
    }

    public static List<Unicode[]> from(String[] sources) {
        List<Unicode[]> array = new ArrayList<>(sources.length);
        for (String source : sources) {
            array.add(from(source));
        }
        return array;
    }

    public static Unicode[] slice(Unicode[] source) {
        return slice(source, 0, source.length);
    }

    public static Unicode[] slice(Unicode[] source, int includedBegin) {
        return slice(source, includedBegin, source.length);
    }

    public static Unicode[] slice(Unicode[] source, int includedBegin, int excludedEnd) {
        int length = excludedEnd - includedBegin;
        Unicode[] output = new Unicode[length];
        System.arraycopy(source, includedBegin, output, 0, length);
        return output;
    }

    public static Unicode[] push(Unicode[] ...items) {
        int size = 0;
        for (Unicode[] item : items) {
            size += item.length;
        }
        Unicode[] combined = new Unicode[size];
        int offset = 0;
        for (Unicode[] item : items) {
            int length = item.length;
            System.arraycopy(item, 0, combined, offset, length);
            offset += length;
        }
        return combined;
    }

    public static Unicode[] push(Unicode firstChar, Unicode[] ...items) {
        int size = 1;
        for (Unicode[] item : items) {
            size += item.length;
        }
        Unicode[] combined = new Unicode[size];
        int offset = 1;
        for (Unicode[] item : items) {
            int length = item.length;
            System.arraycopy(item, 0, combined, offset, length);
            offset += length;
        }
        combined[0] = firstChar;
        return combined;
    }

    public static Unicode[] shuffle(Unicode[] input, Unicode[] salt) {
        if (Objects.isNull(salt) || salt.length == 0) {
            return input;
        }

        Unicode[] transformed = slice(input);

        int integer;
        int saltLength = salt.length;
        for (int i = transformed.length - 1, v = 0, p = 0; i > 0; i--, v++) {
            v %= saltLength; // NOSONAR
            integer = salt[v].codePoint();
            p += integer; // NOSONAR
            int j = (integer + v + p) % i;

            // swap characters at positions i and j
            Unicode a = transformed[i];
            Unicode b = transformed[j];
            transformed[j] = a;
            transformed[i] = b;
        }

        return transformed;
    }

    public static Unicode[] reverse(List<Unicode> alphabets) {
        int size = alphabets.size();
        Unicode[] array = new Unicode[size];
        for (int i = size - 1, j = 0; i >= 0 ; i--) {
            array[j++] = alphabets.get(i); // NOSONAR
        }
        return array;
    }

}
