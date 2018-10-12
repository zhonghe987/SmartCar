package com.finsTcp;

import java.lang.Integer;
import java.lang.String;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.finsTcp.omron.*;
import com.finsTcp.core.types.*;


public class SocketPool{

    private Properties prop;
    private LinkedBlockingQueue<OmronFinsNet> socketPools; 
    private int Max_num_worker;

    public SocketPool(Properties pros){
        this.prop = pros;
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

    public byte stringToByte(String hexs){
        return 0;
    }

    public void init(){
        this.Max_num_worker = Integer.parseInt(this.prop.getProperty("max_worker"));
        this.socketPools = new LinkedBlockingQueue<OmronFinsNet>(Max_num_worker);
        for (int num = 0;num <Max_num_worker; num ++){
            OmronFinsNet client  = this.connect();
            if (client != null){
                socketPools.offer(client);
            }
        }
    }

    public OmronFinsNet connect(){
        OmronFinsNet ofins =null;
        ofins = new OmronFinsNet(this.prop.getProperty("plc_address"),
                                            Integer.parseInt(this.prop.getProperty("plc_port").toString()));
        int sna  = Integer.parseInt(this.prop.getProperty("pc_sna").toString());
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
                return ofins;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ofins;
    }

    public OmronFinsNet getClient() throws InterruptedException{
        if(socketPools.size()==0){
            synchronized (socketPools) {
                int freeConnCount = socketPools.size();
                if(freeConnCount == 0 && freeConnCount < Max_num_worker){
                    OmronFinsNet client = this.connect();
                return client;
                }
            }
        }
        OmronFinsNet client = socketPools.poll(2000,TimeUnit.MILLISECONDS);
        return client;
    }
    
    public void freeConnection(OmronFinsNet client) throws InterruptedException{
        if(null != client && !client.getClosed()){
            socketPools.offer(client);
        }
    }
}