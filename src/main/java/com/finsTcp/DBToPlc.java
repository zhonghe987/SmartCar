package com.FinsTCP;


import com.api.dao.CarInfoDao;
import com.api.model.CarInfo;
import com.FinsTCP.SocketConfig;
import com.FinsTCP.omron.OmronFinsNet;
import com.FinsTCP.exceptions.SocketConnectionException;
import com.FinsTCP.core.types.OperateResult;

public class DBToPlc{
    private String host;
    private int port;
    private byte SA1;
    private byte DA1;
    private byte DA2;
    private OmronFinsNet clientPLC = null;


    public DBToPlc(SocketConfig clientConfig){
        if (null != clientConfig) {
            this.host = clientConfig.getHost();
            this.port = clientConfig.getPort();
            this.SA1  = clientConfig.getSA1();
            this.DA1 = clientConfig.getDA1();
            this.DA2 = clientConfig.getDA2();
        }
    }

    public void connectPLC(){
        clientPLC = new OmronFinsNet(this.host, this.port);
        clientPLC.setSA1(this.SA1); // PC网络号，PC的IP地址的最后一个数
        clientPLC.DA1 = this.DA1; // PLC网络号，PLC的IP地址的最后一个数
        clientPLC.DA2 = this.DA2;
    }
    public void sendToPLC(){

    }

    
    
}