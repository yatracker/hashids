package com.yatracker.js;

import com.yatracker.Hashids;
import com.yatracker.HashidsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ya
 */
class CustomAlphabetTest {

    private static void testAlphabet(String alphabet) {
        Hashids hashids = new HashidsBuilder().alphabet(alphabet).build();
        Number[] numbers = new Number[] {1, 2, 3};
        String id = hashids.encode(numbers);

        Number[] decodedNumbers = hashids.decode(id);
        AssertUtil.equals(numbers, decodedNumbers);
    }

    @Test
    void should_work_with_the_worst_alphabet() {
        testAlphabet("cCsSfFhHuUiItT01");
    }

    @Test
    void should_work_with_an_alphabet_containing_spaces() {
        testAlphabet("cCsSfFhH uUiItT01");
    }
    
    @Test
    void should_work_with_half_the_alphabet_being_separators() {
        testAlphabet("abdegjklCFHISTUc");
    }
    
    @Test
    void should_work_with_exactly_2_separators() {
        testAlphabet("abdegjklmnopqrSF");
    }

    @Test
    void should_work_with_no_separators() {
        testAlphabet("abdegjklmnopqrvwxyzABDEGJKLMNOPQRVWXYZ1234567890");
    }
    
    @Test
    void should_work_with_super_long_alphabet() {
        testAlphabet("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890`~!@#$%^&*()-_=+\\|'\";:/?.>,<{[}]");
    }

    @Test
    void should_work_with_a_weird_alphabet() {
        testAlphabet("`~!@#$%^&*()-_=+\\|'\";:/?.>,<{[}]");
    }

    @Test
    void should_work_with_an_alphabet_with_unicode_chars() {
        testAlphabet("ππππ€£πππππππππππ₯°πππ");
    }

    @Test
    void should_work_with_an_alphabet_with_complex_unicode_chars() {
        testAlphabet("π€Ίπ©πΏβπ¦³ππ©π»π¦·π€¦ββοΈπβπΌβπΎππ½πΈπ°β€οΈπ­");
    }

    @Test
    void should_work_with_alphabet_that_contains_emojis_that_are_subsets_of_each_other() {
        testAlphabet("\uD83D\uDE0D\uD83E\uDDD1\uD83C\uDFFD\u200D\uD83E\uDDB3\uD83E\uDDD1\uD83C\uDF77\uD83D\uDC69\uD83C\uDFFF\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFE\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFD\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFB\u200D\uD83E\uDDB0β\uD83C\uDFFE\uD83D\uDC49\uD83C\uDFFD\uD83D\uDC69\uD83C\uDFFB\uD83E\uDDB7\uD83E\uDD26\u200DβοΈ");
        testAlphabet("\uD83D\uDE0D\uD83E\uDDD1\uD83E\uDDD1\uD83C\uDFFD\u200D\uD83E\uDDB3\uD83C\uDF77\uD83D\uDC69\uD83C\uDFFB\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFF\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFD\u200D\uD83E\uDDB0\uD83D\uDC69\uD83C\uDFFE\u200D\uD83E\uDDB0β\uD83C\uDFFE\uD83D\uDC49\uD83C\uDFFD\uD83D\uDC69\uD83C\uDFFB\uD83E\uDDB7\uD83E\uDD26\u200DβοΈ");
    }

}
