
package com.FinsTCP;

import java.lang.Byte;
import com.FinsTCP.omron.OmronFinsNet;
import com.FinsTCP.exceptions.SocketConnectionException;
import com.FinsTCP.core.types.OperateResult;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SocketClientFactory implements PooledObjectFactory<OmronFinsNet> {
    private String host;
    private int port;


    public SocketClientFactory (SocketConfig clientConfig) {
        if (null != clientConfig) {
            this.host = clientConfig.getHost();
            this.port = clientConfig.getPort();
        }

    }

    public PooledObject<OmronFinsNet> makeObject() throws Exception {
        OmronFinsNet ofnClient  = new OmronFinsNet(this.host, this.port);
        //ofnClient.setSA1(0x10); // PC网络号，PC的IP地址的最后一个数
        ofnClient.DA1 = 0x10; // PLC网络号，PLC的IP地址的最后一个数
        ofnClient.DA2 = 0x00; 
        try
        {
            OperateResult connect = ofnClient.ConnectServer( );
            if (connect.IsSuccess)
            {
                System.out.print( "连接成功！" );
            }else
            {
                System.out.print( "连接失败！" );
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
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