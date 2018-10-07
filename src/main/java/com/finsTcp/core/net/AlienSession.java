package com.finsTcp.core.net;

import java.net.Socket;

/**
 * 异形客户端的对象
 */
public class AlienSession {

    /**
     * 实例化一个默认对�?
     */
    public AlienSession() {
        this.isStatusOk = true;
    }

    /**
     * 获取套接�?
     * 
     * @return
     */
    public java.net.Socket getSocket() {
        return socket;
    }

    /**
     * 设置套接字信�?
     * 
     * @param socket 当前的�??
     */
    public void setSocket(java.net.Socket socket) {
        this.socket = socket;
    }

    /**
     * 获取设备唯一的DTU对象
     * 
     * @return
     */
    public String getDTU() {
        return DTU;
    }

    /**
     * 设置设备的唯�?的DTU信息
     * 
     * @param DTU
     */
    public void setDTU(String DTU) {
        this.DTU = DTU;
    }

    /**
     * 获取当前的连接状态是否正�?
     * 
     * @return
     */
    public boolean getIsStatusOk() {
        return this.isStatusOk;
    }

    /**
     * 设置当前的连接状�?
     * 
     * @param isStatusOk
     */
    public void setIsStatusOk(boolean isStatusOk) {
        this.isStatusOk = isStatusOk;
    }

    private Socket socket = null; // 网络套接�?
    private String DTU = ""; // 唯一的标�?
    private boolean isStatusOk = false; // 当前的网络状�?

}
