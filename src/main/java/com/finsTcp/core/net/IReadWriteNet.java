package com.FinsTCP.core.net;

import com.FinsTCP.core.types.IDataTransfer;
import com.FinsTCP.core.types.OperateResult;
import com.FinsTCP.core.types.OperateResultExOne;


public interface IReadWriteNet {


    OperateResultExOne<byte[]> Read(String address, short length);


    OperateResultExOne<Short> ReadInt16(String address);


    OperateResultExOne<short[]> ReadInt16(String address, short length);


    OperateResultExOne<Integer> ReadInt32(String address);


    OperateResultExOne<int[]> ReadInt32(String address, short length);


    OperateResultExOne<Long> ReadInt64(String address);


    OperateResultExOne<long[]> ReadInt64(String address, short length);


    OperateResultExOne<Float> ReadFloat(String address);


    OperateResultExOne<float[]> ReadFloat(String address, short length);


    OperateResultExOne<Double> ReadDouble(String address);


    OperateResultExOne<double[]> ReadDouble(String address, short length);


    OperateResultExOne<String> ReadString(String address, short length);


    <T extends IDataTransfer> OperateResultExOne<T> ReadCustomer(String address, Class<T> tClass);


    OperateResult Write(String address, short value);


    OperateResult Write(String address, short[] values);


    OperateResult Write(String address, int value);


    OperateResult Write(String address, int[] values);


    OperateResult Write(String address, long value);


    OperateResult Write(String address, long[] values);


    OperateResult Write(String address, float value);


    OperateResult Write(String address, float[] values);


    OperateResult Write(String address, double value);


    OperateResult Write(String address, double[] values);


    OperateResult Write(String address, String value);


    OperateResult Write(String address, String value, int length);


    <T extends IDataTransfer> OperateResult WriteCustomer(String address, T value);

}
