package com.yatracker;

import com.yatracker.unicode.CharUnicode;
import com.yatracker.unicode.DualCharUnicode;
import com.yatracker.unicode.Unicode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ya
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class Hashids {

    private static final long MODULO_PART = 100L;

    private static final int SPLIT_AT_EVERY_NTH = 12;

    private final HashidsUnicodeTable table;

    private static int calcNumberIdInt(Number ...numbers) {
        int numbersIdInt = 0;
        int numberLength = numbers.length;
        for (int i = 0; i < numberLength; i++) {
            Number numberLike = numbers[i];
            if (numberLike instanceof BigInteger) {
                BigInteger number = (BigInteger) numberLike;
                if (number.compareTo(BigInteger.ZERO) < 0) {
                    throw new HashidsException(String.format("Negative number (%s) when encoding is not supported", number));
                }
                numbersIdInt += ((BigInteger) numberLike).mod(BigInteger.valueOf(i + MODULO_PART)).longValue();
            } else {
                long number = numberLike.longValue();
                if (number < 0) {
                    throw new HashidsException(String.format("Negative number (%s) when encoding is not supported", number));
                }
                numbersIdInt += (number % (i + MODULO_PART));
            }
        }

        return numbersIdInt;
    }

    private static int calcExtraNumber(Number numberLike, int charCode) {
        if (numberLike instanceof BigInteger) {
            return ((BigInteger) numberLike).mod(BigInteger.valueOf(charCode)).intValue();
        }
        return (int) (numberLike.longValue() % charCode);
    }

    public String encode(Number ...numbers) {
        if (Objects.isNull(numbers) || numbers.length == 0) {
            return HashidsUnicodeTable.EMPTY_STRING;
        }

        Unicode[] alphabet = table.alphabet;

        int alphabetLength = alphabet.length;
        int numbersIdInt = calcNumberIdInt(numbers);

        UnicodeArrayBuilder ret = new UnicodeArrayBuilder();
        Unicode lottery = alphabet[numbersIdInt % alphabetLength];
        ret.push(lottery);

        Unicode[] seps = table.seps;
        Unicode[] salt = table.salt;

        int numberLength = numbers.length;
        int sepsLength = seps.length;
        for (int i = 0; i < numberLength; i++) {
            Number number = numbers[i];
            Unicode[] buffer = UnicodeArrayUtil.push(lottery, salt, alphabet);
            alphabet = UnicodeArrayUtil.shuffle(alphabet, buffer);

            Unicode[] last = toAlphabet(number, alphabet);
            ret.push(last);

            if (i < numberLength - 1) {
                int charCode = last[0].codePoint() + i;
                int extraNumber = calcExtraNumber(number, charCode);
                ret.push(seps[extraNumber % sepsLength]);
            }
        }

        int minLength = table.minLength;
        if (ret.size() < minLength) {
            Unicode[] guards = table.guards;
            int guardsLength = guards.length;
            int prefixGuardIndex = (numbersIdInt + ret.alphabetAt(0).codePoint()) % guardsLength;
            ret.unshift(guards[prefixGuardIndex]);

            if (ret.size() < minLength) {
                int suffixGuardIndex = (numbersIdInt + ret.alphabetAt(2).codePoint()) % guardsLength;
                ret.push(guards[suffixGuardIndex]);
            }
        }

        int halfLength = alphabetLength / 2;
        while (ret.size() < minLength) {
            alphabet = UnicodeArrayUtil.shuffle(alphabet, alphabet);
            ret.unshift(UnicodeArrayUtil.slice(alphabet, halfLength));
            ret.push(UnicodeArrayUtil.slice(alphabet, 0, halfLength));

            int excess = ret.size() - minLength;
            if (excess > 0) {
                int halfOfExcess = excess / 2;
                ret = ret.slice(halfOfExcess, halfOfExcess + minLength);
            }
        }

        return ret.toString();
    }

    public Number[] decode(String id) {
        if (Objects.isNull(id) || id.isEmpty()) {
            return HashidsUnicodeTable.EMPTY_NUMBER_ARRAY;
        }

        if (!table.allowedCharsRegExp.matcher(id).matches()) {
            throw new HashidsException(String.format("The provided ID (%s) is invalid, as it contains characters that do not match the alphabet table", id));
        }

        String[] idGuardsArray = table.guardsRegExp.split(id);
        int splitIndex = idGuardsArray.length == 3 || idGuardsArray.length == 2 ? 1 : 0;

        String idBreakdown = idGuardsArray[splitIndex];
        if (idBreakdown.isEmpty()) {
            return HashidsUnicodeTable.EMPTY_NUMBER_ARRAY;
        }

        int lotteryCodePoint = idBreakdown.codePointAt(0);
        Unicode lottery;
        int lotteryNextCharIndex;
        if (Character.isSupplementaryCodePoint(lotteryCodePoint)) {
            lottery = new DualCharUnicode(Character.toChars(lotteryCodePoint));
            lotteryNextCharIndex = 2;
        } else {
            lottery = new CharUnicode((char) lotteryCodePoint);
            lotteryNextCharIndex = 1;
        }

        String idSubString = idBreakdown.substring(lotteryNextCharIndex);
        if (idSubString.isEmpty()) {
            return HashidsUnicodeTable.EMPTY_NUMBER_ARRAY;
        }
        String[] ids = table.sepsRegExp.split(idSubString);
        List<Unicode[]> idArray = UnicodeArrayUtil.from(ids);

        Unicode[] alphabet = table.alphabet;
        Number[] result = new Number[idArray.size()];

        Unicode[] salt = table.salt;
        for (int i = 0; i < idArray.size(); i++) {
            Unicode[] subId = idArray.get(i);
            Unicode[] buffer = UnicodeArrayUtil.push(lottery, salt, alphabet);
            alphabet = UnicodeArrayUtil.shuffle(alphabet, buffer);
            result[i] = fromAlphabet(subId, alphabet);
        }

        // if the result is different from what we'd expect, we return an empty result (malformed input):
        if (!Objects.equals(id, encode(result))) {
            return HashidsUnicodeTable.EMPTY_NUMBER_ARRAY;
        }
        return result;
    }

    public String encodeHex(String inputHex) {
        if (Objects.isNull(inputHex) || !HashidsUnicodeTable.HEX_PATTERN.matcher(inputHex).matches()) {
            return HashidsUnicodeTable.EMPTY_STRING;
        }

        Number[] numbers = splitAtIntervalAndMap(inputHex);
        return this.encode(numbers);
    }

    public String decodeHex(String id) {
        Number[] numbers = this.decode(id);
        return Arrays.stream(numbers)
                // the decoded number here is always long type.
                .map(number -> Long.toString(number.longValue(), 16).substring(1))
                .collect(Collectors.joining());
    }

    private static Number[] splitAtIntervalAndMap(String str) {
        int strLength = str.length();
        int size = (int) Math.ceil((double) strLength / SPLIT_AT_EVERY_NTH);
        Number[] numbers = new Number[size];
        for (int i = 0; i < size; i++) {
            int start = i * SPLIT_AT_EVERY_NTH;
            int end = Math.min(start + SPLIT_AT_EVERY_NTH, strLength);
            String s = str.substring(start, end);
            numbers[i] = Long.parseLong("1" + s, 16);
        }
        return numbers;
    }

    private static Unicode[] toAlphabet(Number numberLike, Unicode[] alphabet) {
        List<Unicode> array = new ArrayList<>();
        if (numberLike instanceof BigInteger) {
            BigInteger number = (BigInteger) numberLike;
            BigInteger alphabetLength = BigInteger.valueOf(alphabet.length);
            do {
                array.add(alphabet[number.mod(alphabetLength).intValue()]);
                number = number.divide(alphabetLength);
            } while (number.compareTo(BigInteger.ZERO) > 0);
        } else {
            int alphabetLength = alphabet.length;
            long number = numberLike.longValue();
            do {
                array.add(alphabet[(int) (number % alphabetLength)]);
                number = number / alphabetLength;
            } while (number > 0);
        }

        return UnicodeArrayUtil.reverse(array);
    }

    private static Number fromAlphabet(Unicode[] inputChars, Unicode[] alphabetChars) {
        long carry = 0L;
        BigInteger bigIntCarry = null;

        int alphabetCharsLength = alphabetChars.length;
        long safeLongCarry = (Long.MAX_VALUE / alphabetCharsLength) - 1;
        for (Unicode input : inputChars) {
            int index = indexOf(alphabetChars, input);
            if (index == -1) {
                throw new HashidsException(String.format("The provided ID (%s) is invalid, as it contains characters that do not exist in the alphabet", input));
            }

            if (Objects.isNull(bigIntCarry) && carry >= safeLongCarry) {
                bigIntCarry = BigInteger.valueOf(carry);
            }

            if (Objects.nonNull(bigIntCarry)) {
                bigIntCarry = bigIntCarry.multiply(BigInteger.valueOf(alphabetCharsLength)).add(BigInteger.valueOf(index));
            } else {
                carry = carry * alphabetCharsLength + index;
            }
        }

        if (Objects.isNull(bigIntCarry)) {
            return carry;
        }

        return bigIntCarry.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) <= 0 ? bigIntCarry.longValue() : bigIntCarry;
    }

    private static int indexOf(Unicode[] alphabets, Unicode alphabet) {
        for (int i = 0; i < alphabets.length; i++) {
            if (Objects.equals(alphabet, alphabets[i])) {
                return i;
            }
        }
        return -1;
    }

}
