package com.FinsTCP.core.net;

import com.FinsTCP.core.SoftBasic;
import com.FinsTCP.core.net.INetMessage;
import com.FinsTCP.core.net.AlienSession;
import com.FinsTCP.core.transfer.ByteTransformHelper;
import com.FinsTCP.core.transfer.IByteTransform;
import com.FinsTCP.core.types.OperateResult;
import com.FinsTCP.core.types.OperateResultExOne;
import com.FinsTCP.core.types.OperateResultExTwo;
import com.FinsTCP.StringResources;

import java.lang.reflect.ParameterizedType;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkDoubleBase<TNetMessage extends INetMessage, TTransform extends IByteTransform> extends NetworkBase {

    public NetworkDoubleBase() {
        queueLock = new ReentrantLock();
        byteTransform = getInstanceOfTTransform();
        connectionId = SoftBasic.GetUniqueStringByGuidAndRandom();
    }

    private TTransform byteTransform;
    private String ipAddress = "127.0.0.1";
    private int port = 10000;
    private int connectTimeOut = 10000;
    private int receiveTimeOut = 10000;
    private boolean isPersistentConn = false;
    private Lock queueLock = null;
    private boolean IsSocketError = false;
    private boolean isUseSpecifiedSocket = false;
    private String connectionId = "";

    private TTransform getInstanceOfTTransform() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<TTransform> type = (Class<TTransform>) superClass.getActualTypeArguments()[1];
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TNetMessage getInstanceOfTNetMessage() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<TNetMessage> type = (Class<TNetMessage>) superClass.getActualTypeArguments()[0];
        try {
            return type.newInstance();
        } catch (Exception e) {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }

    public TTransform getByteTransform() {
        return byteTransform;
    }

    public void setByteTransform(TTransform transform) {
        byteTransform = transform;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getReceiveTimeOut() {
        return receiveTimeOut;
    }

    public void setReceiveTimeOut(int receiveTimeOut) {
        this.receiveTimeOut = receiveTimeOut;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        if (!ipAddress.isEmpty()) {
            this.ipAddress = ipAddress;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public AlienSession AlienSession = null;

    public void SetPersistentConnection() {
        isPersistentConn = true;
    }

    public OperateResult ConnectServer() {
        isPersistentConn = true;
        OperateResult result = new OperateResult();

        CloseSocket(CoreSocket);

        OperateResultExOne<Socket> rSocket = CreateSocketAndInitialication();

        if (!rSocket.IsSuccess) {
            IsSocketError = true;
            rSocket.Content = null;
            result.Message = rSocket.Message;
        } else {
            CoreSocket = rSocket.Content;
            result.IsSuccess = true;
               
        }

        return result;
    }

    public OperateResult ConnectServer(AlienSession session) {
        isPersistentConn = true;
        isUseSpecifiedSocket = true;

        if (session != null) {
            if (AlienSession != null)
                CloseSocket(AlienSession.getSocket());

            if (connectionId.isEmpty()) {
                connectionId = session.getDTU();
            }

            if (connectionId == session.getDTU()) {
                CoreSocket = session.getSocket();
                IsSocketError = false;
                AlienSession = session;
                return InitializationOnConnect(session.getSocket());
            } else {
                IsSocketError = true;
                return new OperateResult();
            }
        } else {
            IsSocketError = true;
            return new OperateResult();
        }
    }

    public OperateResult ConnectClose() {
        OperateResult result = new OperateResult();
        isPersistentConn = false;

        queueLock.lock();

        result = ExtraOnDisconnect(CoreSocket);

        if (CoreSocket != null)
            CloseSocket(CoreSocket);
        CoreSocket = null;

        queueLock.unlock();
        return result;
    }

    protected OperateResult InitializationOnConnect(Socket socket) {
        return OperateResult.CreateSuccessResult();
    }

    protected OperateResult ExtraOnDisconnect(Socket socket) {
        return OperateResult.CreateSuccessResult();
    }

    private OperateResultExOne<Socket> GetAvailableSocket() {
        if (isPersistentConn) {
            if (isUseSpecifiedSocket) {
                if (IsSocketError) {
                    OperateResultExOne<Socket> rSocket = new OperateResultExOne<>();
                    rSocket.Message = "连接不可�?";
                    return rSocket;
                } else {

                    return OperateResultExOne.CreateSuccessResult(CoreSocket);
                }
            } else {

                if (IsSocketError || CoreSocket == null) {
                    OperateResult connect = ConnectServer();
                    if (!connect.IsSuccess) {
                        IsSocketError = true;
                        OperateResultExOne<Socket> rSocket = new OperateResultExOne<>();
                        rSocket.Message = connect.Message;
                        rSocket.ErrorCode = connect.ErrorCode;
                        return rSocket;
                    } else {
                        IsSocketError = false;
                        return OperateResultExOne.CreateSuccessResult(CoreSocket);
                    }
                } else {
                    return OperateResultExOne.CreateSuccessResult(CoreSocket);
                }
            }
        } else {

            return CreateSocketAndInitialication();
        }
    }

    private OperateResultExOne<Socket> CreateSocketAndInitialication() {
        OperateResultExOne<Socket> result = CreateSocketAndConnect(ipAddress, port, connectTimeOut);
        if (result.IsSuccess) {

            OperateResult initi = InitializationOnConnect(result.Content);
            if (!initi.IsSuccess) {
                CloseSocket(result.Content);
                result.IsSuccess = initi.IsSuccess;
                result.CopyErrorFromOther(initi);
            }
        }
        return result;
    }

    public OperateResultExOne<byte[]> ReadFromCoreServer(Socket socket, byte[] send) {
        OperateResultExOne<byte[]> result = new OperateResultExOne<byte[]>();

        OperateResultExTwo<byte[], byte[]> read = ReadFromCoreServerBase(socket, send);
        if (read.IsSuccess) {
            result.IsSuccess = read.IsSuccess;
            result.Content = new byte[read.Content1.length + read.Content2.length];
            if (read.Content1.length > 0)
                System.arraycopy(read.Content1, 0, result.Content, 0, read.Content1.length);
            if (read.Content2.length > 0)
                System.arraycopy(read.Content2, 0, result.Content, read.Content1.length, read.Content2.length);
        } else {
            result.CopyErrorFromOther(read);
        }
        return result;
    }

    public OperateResultExOne<byte[]> ReadFromCoreServer(byte[] send) {
        OperateResultExOne<byte[]> result = new OperateResultExOne<byte[]>();
        // string tmp1 = BasicFramework.SoftBasic.ByteToHexString( send, '-' );

        queueLock.lock();

        OperateResultExOne<Socket> resultSocket = GetAvailableSocket();
        if (!resultSocket.IsSuccess) {
            IsSocketError = true;
            if (AlienSession != null)
                AlienSession.setIsStatusOk(false);
            queueLock.unlock();
            result.CopyErrorFromOther(resultSocket);
            return result;
        }

        OperateResultExOne<byte[]> read = ReadFromCoreServer(resultSocket.Content, send);

        if (read.IsSuccess) {
            IsSocketError = false;
            result.IsSuccess = read.IsSuccess;
            result.Content = read.Content;
            // string tmp2 = BasicFramework.SoftBasic.ByteToHexString( result.Content ) ;
        } else {
            IsSocketError = true;
            if (AlienSession != null)
                AlienSession.setIsStatusOk(false);
            result.CopyErrorFromOther(read);
        }

        queueLock.unlock();
        if (!isPersistentConn)
            CloseSocket(resultSocket.Content);
        return result;
    }

    protected OperateResultExTwo<byte[], byte[]> ReadFromCoreServerBase(Socket socket, byte[] send) {
        OperateResultExTwo<byte[], byte[]> result = new OperateResultExTwo<byte[], byte[]>();
        // LogNet?.WriteDebug( ToString( ), "Command: " +
        // BasicFramework.SoftBasic.ByteToHexString( send ) );
        TNetMessage netMsg = getInstanceOfTNetMessage();
        netMsg.setSendBytes(send);

        OperateResult resultSend = Send(socket, send);
        if (!resultSend.IsSuccess) {
            CloseSocket(socket);
            result.CopyErrorFromOther(resultSend);
            return result;
        }

        if (receiveTimeOut >= 0) {

            OperateResultExOne<TNetMessage> resultReceive = ReceiveMessage(socket, receiveTimeOut, netMsg);
            if (!resultReceive.IsSuccess) {
                CloseSocket(socket);
                result.CopyErrorFromOther(resultReceive);
                // result.Message = "Receive data timeout: " + receiveTimeOut;
                return result;
            }

            result.Content1 = resultReceive.Content.getHeadBytes();
            result.Content2 = resultReceive.Content.getContentBytes();
        }

        result.IsSuccess = true;
        return result;
    }

    @Override
    public String toString() {
        return "NetworkDoubleBase<TNetMessage>";
    }

    protected OperateResultExOne<Boolean> GetBoolResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetBoolResultFromBytes(result, byteTransform);
    }

    protected OperateResultExOne<Byte> GetByteResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetByteResultFromBytes(result, byteTransform);
    }

    protected OperateResultExOne<Short> GetInt16ResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetInt16ResultFromBytes(result, byteTransform);
    }

    protected OperateResultExOne<Integer> GetInt32ResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetInt32ResultFromBytes(result, byteTransform);
    }

    protected OperateResultExOne<Long> GetInt64ResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetInt64ResultFromBytes(result, byteTransform);
    }

    protected OperateResultExOne<Float> GetSingleResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetSingleResultFromBytes(result, byteTransform);
    }

    protected OperateResultExOne<Double> GetDoubleResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetDoubleResultFromBytes(result, byteTransform);
    }

    protected OperateResultExOne<String> GetStringResultFromBytes(OperateResultExOne<byte[]> result) {
        return ByteTransformHelper.GetStringResultFromBytes(result, byteTransform);
    }

}
