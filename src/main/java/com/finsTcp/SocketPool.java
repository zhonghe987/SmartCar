package com.finsTcp;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Properties;

import com.finsTcp.omron.*;
import com.finsTcp.core.types.*;


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


public class SocketPool{
    public void connect(Properties prop){
        OmronFinsNet ofins = new OmronFinsNet(prop.getProperty("plc_address"),
                                              Integer.parseInt(prop.getProperty("plc_port").toString()));
        int sna  = Integer.parseInt(prop.getProperty("pc_sna").toString());
        // System.out.print(Byte.decode(prop.getProperty("pc_sna").toString());
        //int snas = Integer.parseInt(IntToHex(sna));
        //System.out.print(Integer.parseInt(IntToHex(sna))); 
        ofins.SNA = 0x20;//0x20; // PC网络号，PC的IP地址的最后一个数
        //int da1 = Integer.parseInt(prop.getProperty("plc_da1").toString());
        ofins.DA1 =  0x10;//0x10; // PLC网络s号，PLC的IP地址的最后一个数
        //int da2 = Integer.parseInt(prop.getProperty("plc_da2").toString());
        
        ofins.DA2 = 0x00;//0x00; // PLC单元号，通常为0
        try {
            OperateResult connect = ofins.ConnectServer();
            if (connect.IsSuccess) {
                System.out.println("连接成功！" );
            } else {
                System.out.println("连接失败！" );
            }
        } catch (Exception ex) {
            System.out.println("error");
        }
    }
}