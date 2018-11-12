package com.FinsTCP.util;

import java.io.ByteArrayOutputStream;
import java.lang.String;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public  final class OmronFinsNetHelper{

    public static String IntToHex(int n){
        char[] ch = new char[20];
        int nIndex = 0;
        while ( true ){
            int m = n/16;
            int k = n%16;
            if ( k == 15 )
                ch[nIndex] = 'F';
            else if ( k == 14 )
                ch[nIndex] = 'E';
            else if ( k == 13 )
                ch[nIndex] = 'D';
            else if ( k == 12 )
                ch[nIndex] = 'C';
            else if ( k == 11 )
                ch[nIndex] = 'B';
            else if ( k == 10 )
                ch[nIndex] = 'A';
            else 
                ch[nIndex] = (char)('0' + k);
            nIndex++;
            if ( m == 0 )
                break;
            n = m;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(ch, 0, nIndex);
        sb.reverse();
        String strHex = new String("0x");
        strHex += sb.toString();
        return strHex;
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
}