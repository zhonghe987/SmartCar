package com.finsTcp.core.net;

import com.finsTcp.core.SoftBasic;
import com.finsTcp.core.net.INetMessage;
import com.finsTcp.core.net.IReadWriteNet;
import com.finsTcp.core.transfer.IByteTransform;
import com.finsTcp.core.types.IDataTransfer;
import com.finsTcp.core.types.OperateResult;
import com.finsTcp.core.types.OperateResultExOne;
import com.finsTcp.Utilities;


public class NetworkDeviceBase<TNetMessage extends INetMessage, TTransform extends IByteTransform>
        extends NetworkDoubleBase<TNetMessage, TTransform> implements IReadWriteNet {


    protected short WordLength = 1;


    public OperateResultExOne<byte[]> Read(String address, short length) {
        return new OperateResultExOne<byte[]>();
    }


    public OperateResult Write(String address, byte[] value) {
        return new OperateResult();
    }


    public <T extends IDataTransfer> OperateResultExOne<T> ReadCustomer(String address, Class<T> tClass) {
        OperateResultExOne<T> result = new OperateResultExOne<T>();
        T Content;
        try {
            Content = tClass.newInstance();
        } catch (Exception ex) {
            Content = null;
        }
        OperateResultExOne<byte[]> read = Read(address, Content.getReadCount());
        if (read.IsSuccess) {
            Content.ParseSource(read.Content);
            result.Content = Content;
            result.IsSuccess = true;
        } else {
            result.ErrorCode = read.ErrorCode;
            result.Message = read.Message;
        }
        return result;
    }


    public <T extends IDataTransfer> OperateResult WriteCustomer(String address, T data) {
        return Write(address, data.ToSource());
    }


    public OperateResultExOne<Short> ReadInt16(String address) {
        return GetInt16ResultFromBytes(Read(address, WordLength));
    }


    public OperateResultExOne<short[]> ReadInt16(String address, short length) {
        OperateResultExOne<byte[]> read = Read(address, (short) (length * WordLength));
        if (!read.IsSuccess) {
            OperateResultExOne<short[]> result = new OperateResultExOne<short[]>();
            result.CopyErrorFromOther(read);
            return result;
        }
        return OperateResultExOne.CreateSuccessResult(super.getByteTransform().TransInt16(read.Content, 0, length));
    }


    public OperateResultExOne<Integer> ReadInt32(String address) {
        return GetInt32ResultFromBytes(Read(address, (short) (2 * WordLength)));
    }


    public OperateResultExOne<int[]> ReadInt32(String address, short length) {
        OperateResultExOne<byte[]> read = Read(address, (short) (length * WordLength * 2));
        if (!read.IsSuccess) {
            OperateResultExOne<int[]> result = new OperateResultExOne<int[]>();
            result.CopyErrorFromOther(read);
            return result;
        }
        return OperateResultExOne.CreateSuccessResult(super.getByteTransform().TransInt32(read.Content, 0, length));
    }

    public OperateResultExOne<Float> ReadFloat(String address) {
        return GetSingleResultFromBytes(Read(address, (short) (2 * WordLength)));
    }


    public OperateResultExOne<float[]> ReadFloat(String address, short length) {
        OperateResultExOne<byte[]> read = Read(address, (short) (length * WordLength * 2));
        if (!read.IsSuccess) {
            OperateResultExOne<float[]> result = new OperateResultExOne<float[]>();
            result.CopyErrorFromOther(read);
            return result;
        }
        return OperateResultExOne.CreateSuccessResult(super.getByteTransform().TransSingle(read.Content, 0, length));
    }


    public OperateResultExOne<Long> ReadInt64(String address) {
        return GetInt64ResultFromBytes(Read(address, (short) (4 * WordLength)));
    }


    public OperateResultExOne<long[]> ReadInt64(String address, short length) {
        OperateResultExOne<byte[]> read = Read(address, (short) (length * WordLength * 4));
        if (!read.IsSuccess) {
            OperateResultExOne<long[]> result = new OperateResultExOne<long[]>();
            result.CopyErrorFromOther(read);
            return result;
        }
        return OperateResultExOne.CreateSuccessResult(super.getByteTransform().TransInt64(read.Content, 0, length));
    }


    public OperateResultExOne<Double> ReadDouble(String address) {
        return GetDoubleResultFromBytes(Read(address, (short) (4 * WordLength)));
    }


    public OperateResultExOne<double[]> ReadDouble(String address, short length) {
        OperateResultExOne<byte[]> read = Read(address, (short) (length * WordLength * 4));
        if (!read.IsSuccess) {
            OperateResultExOne<double[]> result = new OperateResultExOne<double[]>();
            result.CopyErrorFromOther(read);
            return result;
        }
        return OperateResultExOne.CreateSuccessResult(super.getByteTransform().TransDouble(read.Content, 0, length));
    }


    public OperateResultExOne<String> ReadString(String address, short length) {
        return GetStringResultFromBytes(Read(address, length));
    }


    public OperateResult Write(String address, short[] values) {
        return Write(address, super.getByteTransform().TransByte(values));
    }


    public OperateResult Write(String address, short value) {
        return Write(address, new short[] { value });
    }


    public OperateResult Write(String address, int[] values) {
        return Write(address, super.getByteTransform().TransByte(values));
    }


    public OperateResult Write(String address, int value) {
        return Write(address, new int[] { value });
    }


    public OperateResult Write(String address, float[] values) {
        return Write(address, super.getByteTransform().TransByte(values));
    }


    public OperateResult Write(String address, float value) {
        return Write(address, new float[] { value });
    }


    public OperateResult Write(String address, long[] values) {
        return Write(address, getByteTransform().TransByte(values));
    }


    public OperateResult Write(String address, long value) {
        return Write(address, new long[] { value });
    }


    public OperateResult Write(String address, double[] values) {
        return Write(address, getByteTransform().TransByte(values));
    }


    public OperateResult Write(String address, double value) {
        return Write(address, new double[] { value });
    }


    public OperateResult Write(String address, String value) {
        byte[] temp = getByteTransform().TransByte(value, "US-ASCII");
        if (WordLength == 1)
            temp = SoftBasic.ArrayExpandToLengthEven(temp);
        return Write(address, temp);
    }


    public OperateResult Write(String address, String value, int length) {
        byte[] temp = getByteTransform().TransByte(value, "US-ASCII");
        temp = SoftBasic.ArrayExpandToLength(temp, length);
        if (WordLength == 1)
            temp = SoftBasic.ArrayExpandToLengthEven(temp);
        return Write(address, temp);
    }


    public OperateResult WriteUnicodeString(String address, String value) {
        byte[] temp = Utilities.string2Byte(value);
        return Write(address, temp);
    }


    public OperateResult WriteUnicodeString(String address, String value, int length) {
        byte[] temp = Utilities.string2Byte(value);
        temp = SoftBasic.ArrayExpandToLength(temp, length * 2);
        return Write(address, temp);
    }


    @Override
    public String toString() {
        return "NetworkDeviceBase<TNetMessage, TTransform>";
    }

}
