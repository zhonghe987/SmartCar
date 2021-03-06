package com.FinsTCP.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SoftBasic {


    public static String GetSizeDescription(long size) {
        if (size < 1000) {
            return size + " B";
        } else if (size < 1000 * 1000) {
            float data = (float) size / 1024;
            return String.format("%.2f", data) + " Kb";
        } else if (size < 1000 * 1000 * 1000) {
            float data = (float) size / 1024 / 1024;
            return String.format("%.2f", data) + " Mb";
        } else {
            float data = (float) size / 1024 / 1024 / 1024;
            return String.format("%.2f", data) + " Gb";
        }
    }



    public static boolean IsTwoBytesEquel(byte[] b1, int start1, byte[] b2, int start2, int length) {
        if (b1 == null || b2 == null)
            return false;
        for (int i = 0; i < length; i++) {
            if (b1[i + start1] != b2[i + start2]) {
                return false;
            }
        }

        return true;
    }


    public static byte[] ArrayExpandToLength(byte[] data, int length) {
        if (data == null)
            return new byte[0];
        byte[] buffer = new byte[length];
        System.arraycopy(data, 0, buffer, 0, Math.min(data.length, buffer.length));
        return buffer;
    }


    public static byte[] ArrayExpandToLengthEven(byte[] data) {
        if (data == null)
            data = new byte[0];
        if (data.length % 2 == 1) {
            return ArrayExpandToLength(data, data.length + 1);
        } else {
            return data;
        }
    }


    public static <T> T[] ArrayExpandToLengthEven(Class<T> tClass, T[] data) {
        if (data == null)
            data = (T[]) new Object[0];

        if (data.length % 2 == 1) {
            return ArrayExpandToLength(tClass, data, data.length + 1);
        } else {
            return data;
        }
    }


    public static <T> T[] ArrayExpandToLength(Class<T> tClass, T[] data, int length) {
        if (data == null)
            return (T[]) Array.newInstance(tClass, 0);

        if (data.length == length)
            return data;

        T[] buffer = (T[]) Array.newInstance(tClass, length);

        System.arraycopy(data, 0, buffer, 0, Math.min(data.length, buffer.length));

        return buffer;
    }


    public static String GetUniqueStringByGuidAndRandom() {
        Random random = new Random();
        return UUID.randomUUID().toString() + (random.nextInt(9000) + 1000);
    }


    public static String ByteToHexString(byte[] InBytes) {
        return ByteToHexString(InBytes, (char) 0);
    }


    public static String ByteToHexString(byte[] InBytes, char segment) {

        StringBuilder stringBuilder = new StringBuilder("");
        if (InBytes == null || InBytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < InBytes.length; i++) {
            int v = InBytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(segment);
        }
        return stringBuilder.toString();
    }


    public static byte[] BoolArrayToByte(boolean[] array) {
        if (array == null)
            return null;

        int length = array.length % 8 == 0 ? array.length / 8 : array.length / 8 + 1;
        byte[] buffer = new byte[length];

        for (int i = 0; i < array.length; i++) {
            int index = i / 8;
            int offect = i % 8;

            byte temp = 0;
            switch (offect) {
            case 0:
                temp = 0x01;
                break;
            case 1:
                temp = 0x02;
                break;
            case 2:
                temp = 0x04;
                break;
            case 3:
                temp = 0x08;
                break;
            case 4:
                temp = 0x10;
                break;
            case 5:
                temp = 0x20;
                break;
            case 6:
                temp = 0x40;
                break;
            case 7:
                temp = (byte) 0x80;
                break;
            default:
                break;
            }

            if (array[i])
                buffer[index] += temp;
        }

        return buffer;

    }


    public static boolean[] ByteToBoolArray(byte[] InBytes, int length) {
        if (InBytes == null)
            return null;

        if (length > InBytes.length * 8)
            length = InBytes.length * 8;
        boolean[] buffer = new boolean[length];

        for (int i = 0; i < length; i++) {
            int index = i / 8;
            int offect = i % 8;

            byte temp = 0;
            switch (offect) {
            case 0:
                temp = 0x01;
                break;
            case 1:
                temp = 0x02;
                break;
            case 2:
                temp = 0x04;
                break;
            case 3:
                temp = 0x08;
                break;
            case 4:
                temp = 0x10;
                break;
            case 5:
                temp = 0x20;
                break;
            case 6:
                temp = 0x40;
                break;
            case 7:
                temp = (byte) 0x80;
                break;
            default:
                break;
            }

            if ((InBytes[index] & temp) == temp) {
                buffer[i] = true;
            }
        }

        return buffer;
    }


    public static String ByteToHexString(String InString) throws UnsupportedEncodingException {
        return ByteToHexString(InString.getBytes("unicode"));
    }


    private static List<Character> hexCharList = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F');


    public static byte[] HexStringToBytes(String hex) {
        hex = hex.toUpperCase();

        ByteArrayOutputStream ms = new ByteArrayOutputStream();

        for (int i = 0; i < hex.length(); i++) {
            if ((i + 1) < hex.length()) {
                if (hexCharList.contains(hex.charAt(i)) && hexCharList.contains(hex.charAt(i + 1))) {
                    ms.write((byte) (hexCharList.indexOf(hex.charAt(i)) * 16 + hexCharList.indexOf(hex.charAt(i + 1))));
                    i++;
                }
            }
        }

        byte[] result = ms.toByteArray();
        try {
            ms.close();
        } catch (IOException ex) {

        }
        return result;
    }


    public static byte[] SpliceTwoByteArray(byte[] bytes1, byte[] bytes2) {
        if (bytes1 == null && bytes2 == null)
            return null;
        if (bytes1 == null)
            return bytes2;
        if (bytes2 == null)
            return bytes1;

        byte[] buffer = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, buffer, 0, bytes1.length);
        System.arraycopy(bytes2, 0, buffer, bytes1.length, bytes2.length);
        return buffer;
    }

    public static byte[] BytesArrayRemoveBegin(byte[] value, int length) {
        return BytesArrayRemoveDouble(value, length, 0);
    }


    public static byte[] BytesArrayRemoveLast(byte[] value, int length) {
        return BytesArrayRemoveDouble(value, 0, length);
    }


    public static byte[] BytesArrayRemoveDouble(byte[] value, int leftLength, int rightLength) {
        if (value == null)
            return null;
        if (value.length <= (leftLength + rightLength))
            return new byte[0];

        byte[] buffer = new byte[value.length - leftLength - rightLength];
        System.arraycopy(value, leftLength, buffer, 0, buffer.length);

        return buffer;
    }

}
