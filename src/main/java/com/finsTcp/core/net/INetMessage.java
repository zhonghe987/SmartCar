package com.finsTcp.core.net;


public interface INetMessage {


    int ProtocolHeadBytesLength();


    int GetContentLengthByHeadBytes();


    boolean CheckHeadBytesLegal(byte[] token);


    int GetHeadBytesIdentity();


    void setHeadBytes(byte[] headBytes);


    byte[] getHeadBytes();


    byte[] getContentBytes();


    void setContentBytes(byte[] contentBytes);


    byte[] getSendBytes();


    void setSendBytes(byte[] sendBytes);

}
