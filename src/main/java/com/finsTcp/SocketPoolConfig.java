package com.FinsTCP;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class SocketPoolConfig extends GenericObjectPoolConfig {
  public SocketPoolConfig() {
    // defaults to make your life with connection pool easier :)
    setMaxTotal(20);
    setMaxIdle(5);
    setTestWhileIdle(true);
    setMinEvictableIdleTimeMillis(60000);
    setTimeBetweenEvictionRunsMillis(30000);
    setNumTestsPerEvictionRun(-1);
  }
}