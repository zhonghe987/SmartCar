package com.FinsTCP;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private CarInfoDao carInfoDao = new CarInfoDao();
    private static final Logger logger = LoggerFactory.getLogger(DBToPlc.class);


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
        try
        {
            OperateResult connect = clientPLC.ConnectServer( );
            if (connect.IsSuccess)
            {
                logger.info("omron server connect success !");
            }else
            {
                logger.debug("omron server connect faild !");
            }
        }catch (Exception ex)
        {
            throw new SocketConnectionException(ex);
        }

    }
    public void sendToPLC(){
        List<CarInfo> carinfolist = carInfoDao.findAll();
        for(int i = 0; i < carinfolist.size(); i++){
            //carinfolist.get(i);
            carInfoDao.delete(carinfolist.get(i));
        }

    }
    
}