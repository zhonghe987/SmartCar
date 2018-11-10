package com.FinsTCP;

import java.lang.Integer;
import java.lang.String;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.FinsTCP.omron.*;
import com.FinsTCP.Pool;
import com.FinsTCP.SocketConfig;
import com.FinsTCP.SocketClientFactory;
//import com.FinsTCP.util.OmronFinsNetHelper;
//import com.FinsTCP.Protocol;
import com.FinsTCP.core.types.*;



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