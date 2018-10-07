package com.finsTcp.core.net;

import com.finsTcp.core.net.INetMessage;
import com.finsTcp.core.types.HslTimeOut;
import com.finsTcp.core.types.OperateResult;
import com.finsTcp.core.types.OperateResultExOne;
import com.log.ILogNet;
import com.finsTcp.StringResources;
import com.finsTcp.Utilities;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.UUID;


/**
 * 本系统所有网络类的基类，该类为抽象类，无法进行实例化
 */
public abstract class NetworkBase {

    /**
     * 实例化一个NetworkBase对象
     */
    public NetworkBase( )
    {
        Token = UUID.fromString("00000000-0000-0000-0000-000000000000");
    }


    protected Socket CoreSocket = null;


    /**
     * @param timeout
     * @param millisecond
     */
    public static void ThreadPoolCheckConnect(HslTimeOut timeout, int millisecond) {
        while (!timeout.IsSuccessful) {
            if ((new Date().getTime() - timeout.StartTime.getTime()) > millisecond) {
                if (!timeout.IsSuccessful) {
                    try {
                        if (timeout.WorkSocket != null) {
                            timeout.WorkSocket.close();
                        }
                    } catch (java.io.IOException ex) {
                    }
                }
                break;
            }
        }
    }


    protected OperateResultExOne<byte[]> Receive(Socket socket, int length, int timeout )
    {
        OperateResultExOne<byte[]> resultExOne = new OperateResultExOne<>();

        if (length == 0) {
            resultExOne.IsSuccess = true;
            resultExOne.Content = new byte[0];
            return  resultExOne;
        }

        int count_receive = 0;
        byte[] bytes_receive = new byte[length];
        try {
            if(timeout>0) socket.setSoTimeout(timeout);
            InputStream input = socket.getInputStream();
            while (count_receive<length)
            {
                count_receive += input.read(bytes_receive, count_receive, length-count_receive);
            }
        }
        catch (IOException ex)
        {
            CloseSocket(socket);
            resultExOne.Message = ex.getMessage();
            return  resultExOne;
        }

        resultExOne.IsSuccess = true;
        resultExOne.Content = bytes_receive;
        return  resultExOne;
    }


    protected OperateResultExOne<byte[]> Receive(Socket socket, int length )
    {
        return Receive(socket,length,-1);
    }


    protected <TNetMessage extends INetMessage> OperateResultExOne<TNetMessage> ReceiveMessage(Socket socket, int timeOut, TNetMessage netMsg)
    {
        OperateResultExOne<TNetMessage> resultExOne = new OperateResultExOne<>();

        OperateResultExOne<byte[]> headResult = Receive( socket, netMsg.ProtocolHeadBytesLength(), timeOut );
        if (!headResult.IsSuccess)
        {
            resultExOne.CopyErrorFromOther( headResult );
            return resultExOne;
        }

        netMsg.setHeadBytes( headResult.Content );
        if (!netMsg.CheckHeadBytesLegal(Utilities.UUID2Byte(Token)))
        {

            CloseSocket(socket);
            if(LogNet != null) LogNet.WriteError( toString( ), StringResources.Language.TokenCheckFailed() );
            resultExOne.Message = StringResources.Language.TokenCheckFailed();
            return resultExOne;
        }


        int contentLength = netMsg.GetContentLengthByHeadBytes( );
        if (contentLength == 0)
        {
            netMsg.setHeadBytes( new byte[0] );
        }
        else
        {
            OperateResultExOne<byte[]> contentResult = Receive( socket, contentLength, timeOut );
            if (!contentResult.IsSuccess)
            {
                resultExOne.CopyErrorFromOther( contentResult );
                return resultExOne;
            }

            netMsg.setContentBytes( contentResult.Content);
        }

        if (netMsg.getContentBytes() == null){ netMsg.setContentBytes( new byte[0]);}
        resultExOne.Content = netMsg;
        resultExOne.IsSuccess = true;
        return resultExOne;
    }


    protected OperateResult Send(Socket socket,byte[] data){
        OperateResult result = new OperateResult();
        if(data == null) {
            result.IsSuccess = true;
            return result;
        }
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.write(data, 0, data.length);
        }
        catch (IOException ex)
        {
            result.Message = ex.getMessage();
            return  result;
        }

        result.IsSuccess = true;
        return  result;
    }


    protected  OperateResultExOne<Socket> CreateSocketAndConnect(SocketAddress endPoint, int timeOut){
        OperateResultExOne<Socket> operateResultExOne = new OperateResultExOne<>();

        Socket socket = new Socket();
        try {
            socket.connect(endPoint,timeOut);
            operateResultExOne.Content = socket;
            operateResultExOne.IsSuccess = true;
        }
        catch (IOException ex)
        {
            operateResultExOne.Message = ex.getMessage();
            CloseSocket(socket);
        }

        return operateResultExOne;
    }

    protected  OperateResultExOne<Socket> CreateSocketAndConnect(String ipAddress,int port, int timeOut) {
        SocketAddress endPoint = new InetSocketAddress(ipAddress,port);
        return CreateSocketAndConnect(endPoint,timeOut);
    }



    protected OperateResultExOne<Integer> ReadStream(InputStream stream, byte[] buffer) {
        OperateResultExOne<Integer> resultExOne = new OperateResultExOne<>();
        int read_count = 0;
        try {
            while (read_count < buffer.length) {
                read_count += stream.read(buffer, read_count, buffer.length - read_count);
            }
            resultExOne.Content = read_count;
            resultExOne.IsSuccess = true;
        } catch (IOException ex) {
            resultExOne.Message = ex.getMessage();
        }

        return resultExOne;
    }



    protected OperateResult WriteStream(OutputStream stream, byte[] buffer ) {
        OperateResult result = new OperateResultExOne<>();
        try {
            stream.write(buffer, 0, buffer.length);
            result.IsSuccess = true;
        } catch (IOException ex) {
            result.Message = ex.getMessage();
        }

        return result;
    }



    protected void CloseSocket(Socket socket){
        if(socket != null){
            try {
                socket.close();
            }
            catch (Exception ex){

            }
        }
    }



    public ILogNet LogNet = null;


    public UUID Token = null;



    @Override
    public String toString(){
        return "NetworkBase";
    }


}
