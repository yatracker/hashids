package com.yatracker;

import com.yatracker.unicode.Unicode;

import java.util.LinkedList;

/**
 * @author ya
 */
public class UnicodeArrayLinkedView {

    final LinkedList<Unicode[]> queue;

    private int size;

    public UnicodeArrayLinkedView() {
        this.queue = new LinkedList<>();
    }

    public void push(Unicode[] alphabets) {
        queue.add(alphabets);
        this.size += alphabets.length;
    }

    public void unshift(Unicode[] alphabets) {
        queue.addFirst(alphabets);
        this.size += alphabets.length;
    }

    public int size() {
        return size;
    }

    public UnicodeArrayLinkedView slice(int begin, int end) {
        Unicode[] array = array();
        array = UnicodeArrayUtil.slice(array, begin, end);
        UnicodeArrayLinkedView sliced = new UnicodeArrayLinkedView();
        sliced.push(array);
        return sliced;
    }

    public Unicode alphabetAt(int index) {
        int idx = index;
        for (Unicode[] item : queue) {
            int length = item.length;
            if (idx < length) {
                return item[idx];
            }

            idx -= length;
        }

        throw new IndexOutOfBoundsException("Can't index alphabet char at position: " + index);
    }

    public Unicode[] array() {
        int offset = 0;
        Unicode[] combined = new Unicode[size];
        for (Unicode[] item : queue) {
            int length = item.length;
            System.arraycopy(item, 0, combined, offset, length);
            offset += length;
        }
        return combined;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(size);
        for (Unicode[] item : queue) {
            for (Unicode alphabet : item) {
                builder.append(alphabet.toString());
            }
        }
        return builder.toString();
    }
}
