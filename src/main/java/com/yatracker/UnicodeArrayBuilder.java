package com.yatracker;

import com.yatracker.unicode.Unicode;

/**
 * @author ya
 */
public class UnicodeArrayBuilder {

    /**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;

    private static final Unicode[] DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA = new Unicode[0];

    private Unicode[] value;

    private int size;

    public UnicodeArrayBuilder() {
        this.value = DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA;
    }

    public int size() {
        return this.size;
    }

    public int capacity() {
        return this.value.length;
    }

    public Unicode alphabetAt(int index) {
        return this.value[index];
    }

    public UnicodeArrayBuilder slice(int includedBegin, int excludedEnd) {
        UnicodeArrayBuilder builder = new UnicodeArrayBuilder();
        builder.value = UnicodeArrayUtil.slice(this.value, includedBegin, excludedEnd);
        builder.size = excludedEnd - includedBegin;
        return builder;
    }

    public void push(Unicode unicodeChar) {
        int currentSize = this.size;
        int newSize = currentSize + 1;

        Unicode[] scaledValue = ensureCapacityInternal(newSize);
        scaledValue[currentSize] = unicodeChar;
        this.size = newSize;
    }

    public void push(Unicode[] unicodeChars) {
        int currentSize = this.size;
        int unicodeCharsLength = unicodeChars.length;
        int newSize = currentSize + unicodeCharsLength;
        Unicode[] scaledValue = ensureCapacityInternal(newSize);

        System.arraycopy(unicodeChars, 0, scaledValue, currentSize, unicodeCharsLength);
        this.size = newSize;
    }

    public void unshift(Unicode unicodeChar) {
        int currentSize = this.size;
        int newSize = currentSize + 1;
        Unicode[] scaledValue = ensureCapacityInternal(newSize);

        System.arraycopy(scaledValue, 0, scaledValue, 1, currentSize);
        scaledValue[0] = unicodeChar;
        this.size = newSize;
    }

    public void unshift(Unicode[] unicodeChars) {
        int currentSize = this.size;
        int unicodeCharsLength = unicodeChars.length;
        int newSize = currentSize + unicodeCharsLength;
        Unicode[] scaledValue = ensureCapacityInternal(newSize);

        System.arraycopy(scaledValue, 0, scaledValue, unicodeCharsLength, currentSize);
        System.arraycopy(unicodeChars, 0, scaledValue, 0, unicodeCharsLength);
        this.size = newSize;
    }

    private Unicode[] ensureCapacityInternal(int minimumCapacity) {
        Unicode[] oldValue = this.value;
        int oldCapacity = oldValue.length;
        if (oldCapacity == 0) {
            return this.value = new Unicode[DEFAULT_CAPACITY]; // NOSONAR
        }

        if (minimumCapacity <= oldCapacity) {
            return oldValue;
        }

        int growth = minimumCapacity - oldCapacity;
        int prefGrowth = oldCapacity >> 1;
        int prefLength = oldCapacity + Math.max(growth, prefGrowth);

        Unicode[] newValue = new Unicode[prefLength];
        System.arraycopy(oldValue, 0, newValue, 0, this.size);
        return this.value = newValue; // NOSONAR
    }

    @Override
    public String toString() {
        int currentSize = this.size;
        Unicode[] currentValue = this.value;
        StringBuilder builder = new StringBuilder(currentSize);
        for (int i = 0; i < currentSize; i++) {
            builder.append(currentValue[i].toCharArray());
        }
        return builder.toString();
    }

}
