package com.FinsTCP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class Utilities {


    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }


    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        return bytes;
    }


    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }


    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }


    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }


    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }


    public static byte int2OneByte(int num) {
        return (byte) (num & 0x000000ff);
    }


    public static int oneByte2Int(byte byteNum) {

        return byteNum > 0 ? byteNum : 256 + byteNum;
    }


    public static short getShort(byte[] bytes, int index) {
        return (short) ((0xff & bytes[0 + index]) | (0xff00 & (bytes[1 + index] << 8)));
    }


    public static int getInt(byte[] bytes, int index) {
        return (0xff & bytes[0 + index]) | (0xff00 & (bytes[1 + index] << 8)) | (0xff0000 & (bytes[2 + index] << 16))
                | (0xff000000 & (bytes[3 + index] << 24));
    }


    public static long getLong(byte[] bytes, int index) {
        return (0xffL & (long) bytes[0 + index]) | (0xff00L & ((long) bytes[1 + index] << 8))
                | (0xff0000L & ((long) bytes[2 + index] << 16)) | (0xff000000L & ((long) bytes[3 + index] << 24))
                | (0xff00000000L & ((long) bytes[4 + index] << 32))
                | (0xff0000000000L & ((long) bytes[5 + index] << 40))
                | (0xff000000000000L & ((long) bytes[6 + index] << 48))
                | (0xff00000000000000L & ((long) bytes[7 + index] << 56));
    }


    public static float getFloat(byte[] bytes, int index) {
        return Float.intBitsToFloat(getInt(bytes, index));
    }


    public static double getDouble(byte[] bytes, int index) {
        long l = getLong(bytes, index);
        System.out.println(l);
        return Double.longBitsToDouble(l);
    }


    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }


    public static String getString(byte[] bytes, int index, int length, String charsetName) {
        return new String(bytes, index, length, Charset.forName(charsetName));
    }


    public static UUID Byte2UUID(byte[] data) {
        if (data.length != 16) {
            throw new IllegalArgumentException("Invalid UUID byte[]");
        }
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);

        return new UUID(msb, lsb);
    }


    public static byte[] UUID2Byte(UUID uuid) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream(16);
        DataOutputStream da = new DataOutputStream(ba);
        try {
            da.writeLong(uuid.getMostSignificantBits());
            da.writeLong(uuid.getLeastSignificantBits());
            ba.close();
            da.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buffer = ba.toByteArray();

        byte temp = buffer[0];
        buffer[0] = buffer[3];
        buffer[3] = temp;
        temp = buffer[1];
        buffer[1] = buffer[2];
        buffer[2] = temp;

        temp = buffer[4];
        buffer[4] = buffer[5];
        buffer[5] = temp;

        temp = buffer[6];
        buffer[6] = buffer[7];
        buffer[7] = temp;

        return buffer;
    }


    public static byte[] string2Byte(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray;
        try {
            byteArray = str.getBytes("unicode");
        } catch (Exception ex) {
            byteArray = str.getBytes();
        }

        if (byteArray.length >= 2) {
            if (byteArray[0] == -1 && byteArray[1] == -2) {
                byte[] newArray = new byte[byteArray.length - 2];
                System.arraycopy(byteArray, 2, newArray, 0, newArray.length);
                byteArray = newArray;
            } else if (byteArray[0] == -2 && byteArray[1] == -1) {
                for (int i = 0; i < byteArray.length; i++) {
                    byte temp = byteArray[i];
                    byteArray[i] = byteArray[i + 1];
                    byteArray[i + 1] = temp;
                    i++;
                }

                byte[] newArray = new byte[byteArray.length - 2];
                System.arraycopy(byteArray, 2, newArray, 0, newArray.length);
                byteArray = newArray;
            }
        }

        return byteArray;
    }


    public static String byte2String(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }

        for (int i = 0; i < byteArray.length; i++) {
            byte temp = byteArray[i];
            byteArray[i] = byteArray[i + 1];
            byteArray[i + 1] = temp;
            i++;
        }
        String str;
        try {
            str = new String(byteArray, "unicode");
        } catch (Exception ex) {
            str = new String(byteArray);
        }
        return str;
    }


    public static void bytesReverse(byte[] reverse) {
        if (reverse != null) {
            byte tmp = 0;
            for (int i = 0; i < reverse.length / 2; i++) {
                tmp = reverse[i];
                reverse[i] = reverse[reverse.length - 1 - i];
                reverse[reverse.length - 1 - i] = tmp;
            }
        }
    }

    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };


    public static String bytes2HexString(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }


    public static String getStringDateShort(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }
}
