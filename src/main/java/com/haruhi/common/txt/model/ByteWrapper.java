package com.haruhi.common.txt.model;

import java.util.Arrays;

/**
 * @author haruhi0000
 */
public final class ByteWrapper {
    private byte[] bytes;

    public ByteWrapper(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 这里不检查类型，提高效率
     * @param o  the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        ByteWrapper that = (ByteWrapper) o;
        byte[] thatBytes = that.getBytes();
        if (thatBytes.length == bytes.length) {
            for (int i = 0; i < bytes.length; i++) {
                if (thatBytes[i] != bytes[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
