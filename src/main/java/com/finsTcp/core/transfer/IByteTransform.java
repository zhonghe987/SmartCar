package com.finsTcp.core.transfer;

public interface IByteTransform {

    boolean TransBool(byte[] buffer, int index);

    byte TransByte(byte[] buffer, int index);

    byte[] TransByte(byte[] buffer, int index, int length);

    short TransInt16(byte[] buffer, int index);

    short[] TransInt16(byte[] buffer, int index, int length);

    int TransInt32(byte[] buffer, int index);

    int[] TransInt32(byte[] buffer, int index, int length);

    long TransInt64(byte[] buffer, int index);

    long[] TransInt64(byte[] buffer, int index, int length);

    float TransSingle(byte[] buffer, int index);

    float[] TransSingle(byte[] buffer, int index, int length);

    double TransDouble(byte[] buffer, int index);

    double[] TransDouble(byte[] buffer, int index, int length);

    String TransString(byte[] buffer, int index, int length, String encoding);

    byte[] TransByte(boolean value);

    byte[] TransByte(boolean[] values);

    byte[] TransByte(byte value);

    byte[] TransByte(short value);

    byte[] TransByte(short[] values);

    byte[] TransByte(int value);

    byte[] TransByte(int[] values);

    byte[] TransByte(long value);

    byte[] TransByte(long[] values);

    byte[] TransByte(float value);

    byte[] TransByte(float[] values);

    byte[] TransByte(double value);

    byte[] TransByte(double[] values);

    byte[] TransByte(String value, String encoding);

    void setDataFormat(DataFormat dataFormat);

    DataFormat getDataFormat();
}
