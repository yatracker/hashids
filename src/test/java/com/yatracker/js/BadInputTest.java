package com.yatracker.js;

import com.yatracker.Hashids;
import com.yatracker.HashidsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ya
 */
class BadInputTest {

    Hashids hashids = new HashidsBuilder().build();

    @Test
    void should_throw_an_error_when_small_alphabet() {
        try {
            new HashidsBuilder().alphabet("1234567890").build();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("Hashids: alphabet must contain at least 16 unique characters, provided: [1, 2, 3, 4, 5, 6, 7, 8, 9, 0]", e.getMessage());
        }
    }

    @Test
    void should_not_throw_an_error_when_alphabet_has_spaces() {
        Assertions.assertDoesNotThrow(
                () -> new HashidsBuilder().alphabet("a cdefghijklmnopqrstuvwxyz").build()
        );
    }

    @Test
    void should_return_an_empty_string_when_encoding_nothing_or_null() {
        String encoded = hashids.encode();
        Assertions.assertEquals("", encoded);

        Number[] nullArray = null;
        encoded = hashids.encode(nullArray);
        Assertions.assertEquals("", encoded);
    }

    @Test
    void should_return_an_empty_string_when_encoding_a_negative_number() {
        try {
            hashids.encode(-1);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("Negative number (-1) when encoding is not supported", e.getMessage());
        }
    }

    @Test
    void should_return_an_empty_array_when_decoding_nothing() {
        Number[] decoded = hashids.decode("");
        AssertUtil.equals(new Number[0], decoded);

        decoded = hashids.decode(null);
        AssertUtil.equals(new Number[0], decoded);
    }

    @Test
    void should_return_an_empty_array_when_decoding_invalid_id() {
        Number[] decoded = hashids.decode("f");
        AssertUtil.equals(new Number[0], decoded);
    }

    @Test
    void should_return_an_empty_string_when_encoding_non_hex_input() {
        String encoded = hashids.encodeHex("z");
        Assertions.assertEquals("", encoded);
    }
    
    @Test
    void should_return_an_empty_string_when_hex_decoding_invalid_id() {
        String decoded = hashids.decodeHex("f");
        Assertions.assertEquals("", decoded);
    }

    @Test
    void should_throw_an_error_when_an_id_to_be_decoded_contains_chars_that_do_not_exist_in_the_alphabet() {
        try {
            hashids.decode("1234951@#");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertEquals("The provided ID (1234951@#) is invalid, as it contains characters that do not match the alphabet table", e.getMessage());
        }

        Hashids hashids1 = new HashidsBuilder().minLength(6).alphabet("abcdefghjklmnpqrstuvwxyz23456789").build();
        try {
            hashids1.decode("a1bcdef");
        } catch (Exception e) {
            Assertions.assertEquals("The provided ID (a1bcdef) is invalid, as it contains characters that do not match the alphabet table", e.getMessage());
        }
    }
}
