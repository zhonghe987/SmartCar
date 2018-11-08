package com.api.util;

import java.lang.Integer;
import java.lang.String;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.finsTcp.omron.*;
import com.finsTcp.core.types.*;


public class SocketPool{

    private Properties prop;
    private int Max_num_worker;
    public static ConcurrentHashMap<Integer, OmronFinsNet> socketMap = new ConcurrentHashMap<Integer, OmronFinsNet>();
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

    public void init(boolean isAll){
        this.Max_num_worker = Integer.parseInt(this.prop.getProperty("max_worker"));
        for (int i = 0; i < this.Max_num_worker; i++) {
            if(isAll){
                OmronFinsNet client  = this.connect(i, false, true);
                if (client != null){
                    socketMap.put(i, client);
                }
            }else{
                if(socketMap.get(i) == null || socketMap.get(i).getClosed()){
                    OmronFinsNet client  = this.connect(i, false, true);
                    if (client != null){
                        socketMap.put(i, client);
                    }
                }	
            }					
		}
    }

    public OmronFinsNet connect(Integer id, boolean closed, boolean free){
        OmronFinsNet ofins = null;
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

        ofins.setId(id);
        ofins.setClosed(closed);
        ofins.setFreed(free);
        try {
            OperateResult socketConnect = ofins.ConnectServer();
            if (socketConnect.IsSuccess) {
                System.out.println("连接成功！" );
                return ofins;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ofins;
    }

    public OmronFinsNet getClient(){
        OmronFinsNet client = null;
        if (socketMap.size() < this.Max_num_worker ){
            this.init(false);
        }
        if(socketMap.size() > 0){
            for (Map.Entry<Integer, OmronFinsNet> entry : socketMap.entrySet()) {
				client = entry.getValue();
				if(client.getFreed() && ! client.getClosed()){
					client.setFreed(false);
                    return client;
                }
            }
        }
        client = this.connect(-1, false, true);
        return client;
    }
    
    public void distoryClient(int socketId){
		OmronFinsNet client = socketMap.get(socketId);
		client.setFreed(true);
    }

    public void freeConnection(OmronFinsNet client){
        if(client == null){
            return;
        }
		if( !client.getClosed()){
            this.distoryClient(client.getId());
            return;
        }
    }
}