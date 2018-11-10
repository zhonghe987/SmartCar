package com.FinsTCP.core.transfer;

import com.FinsTCP.core.SoftBasic;
import com.FinsTCP.Utilities;


public class ReverseWordTransform extends ByteTransformBase {

    public ReverseWordTransform() {
        this.setDataFormat(DataFormat.ABCD);
    }


    private byte[] ReverseBytesByWord(byte[] buffer, int index, int length) {
        byte[] tmp = new byte[length];

        for (int i = 0; i < length; i++) {
            tmp[i] = buffer[index + i];
        }

        for (int i = 0; i < length / 2; i++) {
            byte b = tmp[i * 2 + 0];
            tmp[i * 2 + 0] = tmp[i * 2 + 1];
            tmp[i * 2 + 1] = b;
        }

        return tmp;
    }

    private byte[] ReverseBytesByWord(byte[] buffer) {
        return ReverseBytesByWord(buffer, 0, buffer.length);
    }


    public boolean IsStringReverse = false;


    @Override
    public short TransInt16(byte[] buffer, int index) {
        return Utilities.getShort(ReverseBytesByWord(buffer, index, 2), 0);
    }


    @Override
    public String TransString(byte[] buffer, int index, int length, String encoding) {
        byte[] tmp = TransByte(buffer, index, length);

        if (IsStringReverse) {
            return Utilities.getString(ReverseBytesByWord(tmp), "ASCII");
        } else {
            return Utilities.getString(tmp, "ASCII");
        }
    }


    @Override
    public byte[] TransByte(boolean[] values) {
        return SoftBasic.BoolArrayToByte(values);
    }


    @Override
    public byte[] TransByte(short[] values) {
        if (values == null)
            return null;

        byte[] buffer = new byte[values.length * 2];
        for (int i = 0; i < values.length; i++) {
            byte[] tmp = Utilities.getBytes(values[i]);
            System.arraycopy(tmp, 0, buffer, 2 * i, tmp.length);
        }

        return ReverseBytesByWord(buffer);
    }


    @Override
    public byte[] TransByte(String value, String encoding) {
        if (value == null)
            return null;
        byte[] buffer = Utilities.getBytes(value, encoding);
        buffer = SoftBasic.ArrayExpandToLengthEven(buffer);
        if (IsStringReverse) {
            return ReverseBytesByWord(buffer);
        } else {
            return buffer;
        }
    }

}
