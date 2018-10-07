package com.finsTcp.core.transfer;

import com.finsTcp.Utilities;



public class ByteTransformBase implements IByteTransform {


    public ByteTransformBase(){
        this.dataFormat = DataFormat.DCBA;
    }


    public boolean TransBool(byte[] buffer, int index) {
        return buffer[index] != 0x00;
    }


    public byte TransByte(byte[] buffer, int index) {
        return buffer[index];
    }


    public byte[] TransByte(byte[] buffer, int index, int length) {
        byte[] tmp = new byte[length];
        System.arraycopy(buffer, index, tmp, 0, length);
        return tmp;
    }


    public short TransInt16(byte[] buffer, int index) {
        return Utilities.getShort(buffer, index);
    }


    public short[] TransInt16(byte[] buffer, int index, int length) {
        short[] tmp = new short[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = TransInt16(buffer, index + 2 * i);
        }
        return tmp;
    }


    public int TransInt32(byte[] buffer, int index) {
        return Utilities.getInt(ByteTransDataFormat4(buffer, index),0);
    }



    public int[] TransInt32(byte[] buffer, int index, int length) {
        int[] tmp = new int[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = TransInt32(buffer, index + 4 * i);
        }
        return tmp;
    }



    public long TransInt64(byte[] buffer, int index) {
        return Utilities.getLong(ByteTransDataFormat8(buffer, index),0);
    }



    public long[] TransInt64(byte[] buffer, int index, int length) {
        long[] tmp = new long[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = TransInt64(buffer, index + 8 * i);
        }
        return tmp;
    }



    public float TransSingle(byte[] buffer, int index) {
        return Utilities.getFloat(ByteTransDataFormat4(buffer, index),0);
    }



    public float[] TransSingle(byte[] buffer, int index, int length) {
        float[] tmp = new float[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = TransSingle(buffer, index + 4 * i);
        }
        return tmp;
    }



    public double TransDouble(byte[] buffer, int index) {
        return Utilities.getDouble(ByteTransDataFormat8(buffer, index),0);
    }



    public double[] TransDouble(byte[] buffer, int index, int length) {
        double[] tmp = new double[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = TransDouble(buffer, index + 8 * i);
        }
        return tmp;
    }



    public String TransString(byte[] buffer, int index, int length, String encoding) {
        return Utilities.getString(TransByte(buffer, index, length), encoding);
    }



    public byte[] TransByte(boolean value) {
        return TransByte(new boolean[]{value});
    }



    public byte[] TransByte(boolean[] values) {
        if (values == null) return null;

        byte[] buffer = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i]) buffer[i] = 0x01;
        }

        return buffer;
    }



    public byte[] TransByte(byte value) {
        return new byte[]{value};
    }


    public byte[] TransByte(short value) {
        return TransByte(new short[]{value});
    }


    public byte[] TransByte(short[] values) {
        if (values == null) return null;
        byte[] buffer = new byte[values.length * 2];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(Utilities.getBytes(values[i]), 0, buffer, 2 * i, 2);
        }
        return buffer;
    }


    public byte[] TransByte(int value) {
        return TransByte(new int[]{value});
    }


    public byte[] TransByte(int[] values) {
        if (values == null) return null;

        byte[] buffer = new byte[values.length * 4];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(ByteTransDataFormat4(Utilities.getBytes(values[i])), 0, buffer, 4 * i, 4);
        }

        return buffer;
    }


    public byte[] TransByte(long value) {
        return TransByte(new long[]{value});
    }



    public byte[] TransByte(long[] values) {
        if (values == null) return null;

        byte[] buffer = new byte[values.length * 8];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(ByteTransDataFormat8(Utilities.getBytes(values[i])), 0, buffer, 8 * i, 8);
        }

        return buffer;
    }



    public byte[] TransByte(float value) {
        return TransByte(new float[]{value});
    }



    public byte[] TransByte(float[] values) {
        if (values == null) return null;

        byte[] buffer = new byte[values.length * 4];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(ByteTransDataFormat4(Utilities.getBytes(values[i])), 0, buffer, 4 * i, 4);
        }

        return buffer;
    }



    public byte[] TransByte(double value) {
        return TransByte(new double[]{value});
    }


    public byte[] TransByte(double[] values) {
        if (values == null) return null;

        byte[] buffer = new byte[values.length * 8];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(ByteTransDataFormat8(Utilities.getBytes(values[i])), 0, buffer, 8 * i, 8);
        }

        return buffer;
    }


    public byte[] TransByte(String value, String encoding) {
        return Utilities.getBytes(value, encoding);
    }


    private DataFormat dataFormat = DataFormat.ABCD;


    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }


    public DataFormat getDataFormat() {
        return this.dataFormat;
    }



    protected byte[] ByteTransDataFormat4(byte[] value, int Index) {
        byte[] buffer = new byte[4];
        switch (dataFormat) {
            case ABCD: {
                buffer[0] = value[Index + 3];
                buffer[1] = value[Index + 2];
                buffer[2] = value[Index + 1];
                buffer[3] = value[Index + 0];
                break;
            }
            case BADC: {
                buffer[0] = value[Index + 2];
                buffer[1] = value[Index + 3];
                buffer[2] = value[Index + 0];
                buffer[3] = value[Index + 1];
                break;
            }
            case CDAB: {
                buffer[0] = value[Index + 1];
                buffer[1] = value[Index + 0];
                buffer[2] = value[Index + 3];
                buffer[3] = value[Index + 2];
                break;
            }
            case DCBA: {
                buffer[0] = value[Index + 0];
                buffer[1] = value[Index + 1];
                buffer[2] = value[Index + 2];
                buffer[3] = value[Index + 3];
                break;
            }
        }
        return buffer;
    }



    protected byte[] ByteTransDataFormat4(byte[] value) {
        return ByteTransDataFormat4(value, 0);
    }



    protected byte[] ByteTransDataFormat8(byte[] value, int Index) {
        byte[] buffer = new byte[8];
        switch (dataFormat) {
            case ABCD: {
                buffer[0] = value[Index + 7];
                buffer[1] = value[Index + 6];
                buffer[2] = value[Index + 5];
                buffer[3] = value[Index + 4];
                buffer[4] = value[Index + 3];
                buffer[5] = value[Index + 2];
                buffer[6] = value[Index + 1];
                buffer[7] = value[Index + 0];
                break;
            }
            case BADC: {
                buffer[0] = value[Index + 6];
                buffer[1] = value[Index + 7];
                buffer[2] = value[Index + 4];
                buffer[3] = value[Index + 5];
                buffer[4] = value[Index + 2];
                buffer[5] = value[Index + 3];
                buffer[6] = value[Index + 0];
                buffer[7] = value[Index + 1];
                break;
            }

            case CDAB: {
                buffer[0] = value[Index + 1];
                buffer[1] = value[Index + 0];
                buffer[2] = value[Index + 3];
                buffer[3] = value[Index + 2];
                buffer[4] = value[Index + 5];
                buffer[5] = value[Index + 4];
                buffer[6] = value[Index + 7];
                buffer[7] = value[Index + 6];
                break;
            }
            case DCBA: {
                buffer[0] = value[Index + 0];
                buffer[1] = value[Index + 1];
                buffer[2] = value[Index + 2];
                buffer[3] = value[Index + 3];
                buffer[4] = value[Index + 4];
                buffer[5] = value[Index + 5];
                buffer[6] = value[Index + 6];
                buffer[7] = value[Index + 7];
                break;
            }
        }
        return buffer;
    }



    protected byte[] ByteTransDataFormat8(byte[] value) {
        return ByteTransDataFormat8(value, 0);
    }
}
