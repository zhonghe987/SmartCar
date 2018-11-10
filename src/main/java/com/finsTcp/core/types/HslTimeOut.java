package com.FinsTCP.core.types;
import java.net.Socket;
import java.util.Date;

public class HslTimeOut {


    /**
     * 默认的无参构造函
     */
    public HslTimeOut()
    {
        StartTime = new Date();
        IsSuccessful = false;
    }


    /**
     * 操作的开始时
     */
    public Date StartTime = null;


    /**
     * 操作是否成功
     */
    public boolean IsSuccessful = false;


    /**
     * 延时的时间，单位：毫s
     */
    public int DelayTime = 5000;


    /**
     * 当前的网络
     */
    public Socket WorkSocket = null;

}
