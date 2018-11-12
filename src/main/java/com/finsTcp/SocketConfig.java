package com.FinsTCP;

import java.io.InputStream;
import java.util.Properties;
import com.FinsTCP.util.OmronFinsNetHelper;

public class SocketConfig{
    private String host = "127.0.0.1";
    private int port = 6900;
    private byte SA1;
    private byte DA1;
    private byte DA2;

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
        this.SA1 = Byte.parseByte(pp.getProperty("plc_sa1"));
        this.DA1 = Byte.parseByte(pp.getProperty("plc_da1"));
        this.DA2 = Byte.parseByte(pp.getProperty("plc_da2"));
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }

    public byte getSA1(){
        return this.SA1;
    }

    public byte getDA1(){
        this.DA1 = OmronFinsNetHelper.HexStringToBytes(pp.getProperty("plc_da1")).;
        return this.DA1;
    }

    public byte getDA2(){
        return this.DA2;
    }

    public String toString(){
        return "host = " + this.host + ":port =" +this.port;
    }
}