package com.FinsTCP.omron;


public class OmronFinsDataType {

    public OmronFinsDataType(byte bitCode, byte wordCode) {
        BitCode = bitCode;
        WordCode = wordCode;

    }


    public byte getBitCode() {
        return BitCode;
    }


    public byte getWordCode() {
        return WordCode;
    }

    private byte BitCode = 0;
    private byte WordCode = 0;


    public static final OmronFinsDataType DM = new OmronFinsDataType((byte) 0x02, (byte) 0x82);


    public static final OmronFinsDataType CIO = new OmronFinsDataType((byte) 0x30, (byte) 0xB0);


    public static final OmronFinsDataType WR = new OmronFinsDataType((byte) 0x31, (byte) 0xB1);


    public static final OmronFinsDataType HR = new OmronFinsDataType((byte) 0x32, (byte) 0xB2);


    public static final OmronFinsDataType AR = new OmronFinsDataType((byte) 0x33, (byte) 0xB3);
}
