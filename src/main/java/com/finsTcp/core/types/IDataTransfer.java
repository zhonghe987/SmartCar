package com.finsTcp.core.types;


public interface IDataTransfer {


    short getReadCount();


    void ParseSource(byte[] Content);


    byte[] ToSource();
}
