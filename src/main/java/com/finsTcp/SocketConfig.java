package com.FinsTCP;

import java.io.InputStream;
import java.util.Properties;

public class SocketConfig{
    private String host = "127.0.0.1";
    private int port = 6900;

    static Properties pp;
 
	static{
		pp = new Properties();
		try {
			InputStream fps = SocketConfig.class.getResourceAsStream("/finsTCPConfig.properties");
			pp.load(fps);
			fps.close();
		} catch (Exception e) {
			System.out.print("读取config.properties文件异常!");
			e.printStackTrace();
		}
	}


    public SocketConfig(){
        parseConfig();
    }

    private void parseConfig(){
        this.host = pp.getProperty("plc_address");
        this.port = Integer.parseInt(pp.getProperty("plc_port"));
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }

    public String toString(){
        return "host = " + this.host + ":port =" +this.port;
    }
}