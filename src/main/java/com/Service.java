package com;


import java.lang.String;
import java.lang.System;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.Properties;
//import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import org.eclipse.jetty.server.Server;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;



public class Service {

    //private static final String CONFIG_NAME = "config.properties";
    private static Properties prop;

    private static void init() {
        prop = new Properties();
        String filepath = System.getProperty("user.dir") + "/conf/config.properties";    
        InputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(filepath)); 
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        Server server = new Server(8282); // 监听8282端口
        ServletHolder servlet = new ServletHolder(ServletContainer.class);
        // 设置初始化参数
        servlet.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        servlet.setInitParameter("com.sun.jersey.config.property.packages", "com.api");
        servlet.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true"); // 自动将对象映射成json返回
        ServletContextHandler handler = new ServletContextHandler(NO_SESSIONS);
        handler.setContextPath("/");
        handler.addServlet(servlet, "/*");
        server.setHandler(handler);
        System.out.println("start...in 8282");
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }

    }
}