package com.finsTcp;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.lang.System;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.Properties;
//import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.finsTcp.omron.*;
import com.finsTcp.core.types.*;

public class Server {

    //private static final String CONFIG_NAME = "config.properties";
    private static Properties prop;

    private static void init() {
        prop = new Properties();
        //String filepath = App.class.getClassLoader().getResource(CONFIG_NAME).getPath();
        //System.out.print(System.getProperty("usder.dir"));
        String filepath = System.getProperty("user.dir") + "/conf/config.properties";    
        //InputStream fis = new BufferedInputStream(new FileInputStream(filePath));  
        //FileInputStream fis = null;
        InputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(filepath)); 
            //fis = new FileInputStream(filepath);
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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


    public static void main(String[] args) {
        init();
        OmronFinsNet ofins = new OmronFinsNet(prop.getProperty("plc_address"),
                                              Integer.parseInt(prop.getProperty("plc_port").toString()));
        //int sna  = Integer.parseInt(prop.getProperty("pc_sna").toString());
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