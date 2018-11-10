
package com.FinsTCP;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.FinsTCP.omron.OmronFinsNet;
import com.FinsTCP.exceptions.SocketConnectionException;
import com.FinsTCP.core.types.OperateResult;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SocketClientFactory implements PooledObjectFactory<OmronFinsNet> {
    private String host;
    private int port;
    private byte SA1;
    private byte DA1;
    private byte DA2;
    private static final Logger logger = LoggerFactory.getLogger(SocketClientFactory.class);


    public SocketClientFactory (SocketConfig clientConfig) {
        if (null != clientConfig) {
            this.host = clientConfig.getHost();
            this.port = clientConfig.getPort();
            this.SA1  = clientConfig.getSA1();
            this.DA1 = clientConfig.getDA1();
            this.DA2 = clientConfig.getDA2();
        }

    }

    public PooledObject<OmronFinsNet> makeObject() throws Exception {
        OmronFinsNet ofnClient  = new OmronFinsNet(this.host, this.port);
        ofnClient.setSA1(this.SA1); // PC网络号，PC的IP地址的最后一个数
        ofnClient.DA1 = this.DA1; // PLC网络号，PLC的IP地址的最后一个数
        ofnClient.DA2 = this.DA2; 
        try
        {
            OperateResult connect = ofnClient.ConnectServer( );
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

        return new DefaultPooledObject<OmronFinsNet>(ofnClient);

     }

    public void destroyObject(PooledObject<OmronFinsNet> pooledObject) throws Exception {
        OmronFinsNet ofnClient = pooledObject.getObject();
        try {
            ofnClient.ConnectClose( );
        } catch (Exception ex) {
            throw new SocketConnectionException("Exception in closing transport client connection.",ex);
        }
    }

    public boolean validateObject(PooledObject<OmronFinsNet> pooledObject) {
        try {
            OmronFinsNet ofnClient = (OmronFinsNet) pooledObject.getObject();
            return ofnClient.getClosed();
        } catch (Exception ex) {
            return false;
        }
    }

    public void activateObject(PooledObject<OmronFinsNet> pooledObject) throws Exception {

    }

    public void passivateObject(PooledObject<OmronFinsNet> pooledObject) throws Exception {

    }
}