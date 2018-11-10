package com.FinsTCP;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.FinsTCP.omron.*;
import com.FinsTCP.Pool;
import com.FinsTCP.SocketConfig;
import com.FinsTCP.SocketClientFactory;


public class SocketPool extends Pool<OmronFinsNet>{

    public SocketPool(){
        
    }
    public SocketPool(final GenericObjectPoolConfig poolConfig,  SocketConfig socketConfig) {
        super(poolConfig, new SocketClientFactory(socketConfig));
    }

    public OmronFinsNet getResource() {
        OmronFinsNet client = (OmronFinsNet) super.getResource();
        return client;
    }

    public void returnResource(OmronFinsNet client) {
        super.returnResource(client);
    }
}