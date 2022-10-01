package com.yatracker;

import com.yatracker.unicode.Unicode;
import com.yatracker.unicode.DualCharUnicode;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author ya
 */
final class HashidsUnicodeTable {

    public static final String EMPTY_STRING = "";
    public static final Number[] EMPTY_NUMBER_ARRAY = new Number[0];

    static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    static final String DEFAULT_SEPS = "cfhistuCFHISTU";

    static final Pattern HEX_PATTERN = Pattern.compile("^[\\dA-Fa-f]+$");

    private static final Set<String> NEED_ESCAPE_REGEX_CHARS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"))
    );

    private static final int MIN_ALPHABET_LENGTH = 16;
    private static final double SEPARATOR_DIV = 3.5;
    private static final int GUARD_DIV = 12;

    final Unicode[] seps;

    final Unicode[] alphabet;

    final Unicode[] salt;

    final Unicode[] guards;

    final int minLength;

    final Pattern guardsRegExp;
    final Pattern sepsRegExp;
    final Pattern allowedCharsRegExp;

    HashidsUnicodeTable(String salt, int minLength, String alphabet, String seps) {
        this.salt = UnicodeArrayUtil.from(salt);
        this.minLength = minLength;

        Set<Unicode> uniqAlphabet = unique(alphabet);
        if (uniqAlphabet.size() < MIN_ALPHABET_LENGTH) {
            throw new HashidsException(String.format("Hashids: alphabet must contain at least %s unique characters, provided: %s", MIN_ALPHABET_LENGTH, uniqAlphabet));
        }

        Set<Unicode> uniqSeps = unique(seps);
        // `alphabet` should not contains `seps`
        Unicode[] alphabetArray = filter(uniqAlphabet, item -> !uniqSeps.contains(item));

        // `seps` should contain only characters present in `alphabet`
        Unicode[] sepsArray = filter(uniqSeps, uniqAlphabet::contains);
        sepsArray = UnicodeArrayUtil.shuffle(sepsArray, this.salt);

        int sepsLength;
        int diff;

        if (sepsArray.length == 0 ||  (((double) alphabetArray.length) / sepsArray.length) > SEPARATOR_DIV) {
            sepsLength = (int) Math.ceil((alphabetArray.length) / SEPARATOR_DIV);

            if (sepsLength > sepsArray.length) {
                diff = sepsLength - sepsArray.length;
                sepsArray = UnicodeArrayUtil.push(sepsArray, UnicodeArrayUtil.slice(alphabetArray, 0, diff));
                alphabetArray = UnicodeArrayUtil.slice(alphabetArray, diff);
            }
        }

        alphabetArray = UnicodeArrayUtil.shuffle(alphabetArray, this.salt);
        int guardCount = (int) Math.ceil(((double) alphabetArray.length) / GUARD_DIV);

        if (alphabetArray.length < 3) {
            this.guards = UnicodeArrayUtil.slice(sepsArray, 0, guardCount);
            sepsArray = UnicodeArrayUtil.slice(sepsArray, guardCount);
        } else {
            this.guards = UnicodeArrayUtil.slice(alphabetArray, 0, guardCount);
            alphabetArray = UnicodeArrayUtil.slice(alphabetArray, guardCount);
        }

        this.seps = sepsArray;
        this.alphabet = alphabetArray;

        this.guardsRegExp = makeAnyOfCharsRegExp(this.guards);
        this.sepsRegExp = makeAnyOfCharsRegExp(this.seps);
        this.allowedCharsRegExp = makeAtLeastSomeCharRegExp(UnicodeArrayUtil.push(this.alphabet, this.guards, this.seps));
    }

    private static Set<Unicode> unique(String source) {
        Unicode[] alphabets = UnicodeArrayUtil.from(source);
        return new LinkedHashSet<>(Arrays.asList(alphabets));
    }

    private static Unicode[] filter(Set<Unicode> set, Predicate<? super Unicode> filter) {
        return set.stream().filter(filter).toArray(Unicode[]::new);
    }

    private static Pattern makeAnyOfCharsRegExp(Unicode[] chars) {
        String regex = Arrays.stream(chars)
                .map(HashidsUnicodeTable::escapeRegExp)
                // we need to sort these from longest to shortest,
                // as they may contain multibyte unicode characters (these should come first)
                .sorted((a, b) -> b.length() - a.length())
                .collect(Collectors.joining("|"));
        return Pattern.compile(regex);
    }

    private static Pattern makeAtLeastSomeCharRegExp(Unicode[] chars) {
        String regex = Arrays.stream(chars)
                .map(HashidsUnicodeTable::escapeRegExp)
                // we need to sort these from longest to shortest,
                // as they may contain multibyte unicode characters (these should come first)
                .sorted((a, b) -> b.length() - a.length())
                .collect(Collectors.joining());
        return Pattern.compile("^[" + regex +"]+$");
    }

    private static String escapeRegExp(Unicode alphabet) {
        if (alphabet instanceof DualCharUnicode) {
            return alphabet.toString();
        }
        String value = alphabet.toString();
        if (NEED_ESCAPE_REGEX_CHARS.contains(value)) {
            return "\\" + value;
        }

        return value;
    }

}
