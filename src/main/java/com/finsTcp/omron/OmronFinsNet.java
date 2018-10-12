package com.finsTcp.omron;

import com.finsTcp.core.net.FinsMessage;
import com.finsTcp.core.net.NetworkDeviceBase;
import com.finsTcp.core.transfer.ReverseWordTransform;
import com.finsTcp.core.types.OperateResult;
import com.finsTcp.core.types.OperateResultExOne;
import com.finsTcp.core.types.OperateResultExTwo;
import com.finsTcp.StringResources;
import com.finsTcp.Utilities;

import java.net.Socket;

public class OmronFinsNet extends NetworkDeviceBase<FinsMessage, ReverseWordTransform> {

    public OmronFinsNet() {
        WordLength = 1;
    }


    public OmronFinsNet(String ipAddress, int port) {
        WordLength = 1;
        setIpAddress(ipAddress);
        setPort(port);
    }
    public boolean closed = false;

    public byte ICF = (byte) 0x80;


    public byte RSV = 0x00;


    public byte GCT = 0x02;


    public byte DNA = 0x00;


    public byte DA1 = 0x13;


    public byte DA2 = 0x00;


    public byte SNA = 0x00;

    private byte computerSA1 = 0x0B;


    public byte getSA1() {
        return computerSA1;
    }

    public void setClosed(boolean flag){
        this.closed = flag;
    }

    public boolean getClosed(){
        return this.closed;
    }


    public void setSA1(byte computerSA1) {
        this.computerSA1 = computerSA1;
        handSingle[19] = computerSA1;
    }


    public byte SA2 = 0x00;


    public byte SID = 0x00;


    private byte[] PackCommand(byte[] cmd) {
        byte[] buffer = new byte[26 + cmd.length];
        System.arraycopy(handSingle, 0, buffer, 0, 4);

        byte[] tmp = Utilities.getBytes(buffer.length - 8);
        Utilities.bytesReverse(tmp);

        System.arraycopy(tmp, 0, buffer, 4, tmp.length);
        buffer[11] = 0x02;

        buffer[16] = ICF;
        buffer[17] = RSV;
        buffer[18] = GCT;
        buffer[19] = DNA;
        buffer[20] = DA1;
        buffer[21] = DA2;
        buffer[22] = SNA;
        buffer[23] = getSA1();
        buffer[24] = SA2;
        buffer[25] = SID;
        System.arraycopy(cmd, 0, buffer, 26, cmd.length);

        return buffer;
    }


    private OperateResultExOne<byte[]> BuildReadCommand(String address, int length, boolean isBit) {
        OperateResultExTwo<OmronFinsDataType, byte[]> analysis = AnalysisAddress(address, isBit);
        if (!analysis.IsSuccess)
            return OperateResultExOne.<byte[]>CreateFailedResult(analysis);

        byte[] _PLCCommand = new byte[8];
        _PLCCommand[0] = 0x01;
        _PLCCommand[1] = 0x01;
        if (isBit) {
            _PLCCommand[2] = analysis.Content1.getBitCode();
        } else {
            _PLCCommand[2] = analysis.Content1.getWordCode();
        }

        System.arraycopy(analysis.Content2, 0, _PLCCommand, 3, analysis.Content2.length);
        _PLCCommand[6] = (byte) (length / 256);
        _PLCCommand[7] = (byte) (length % 256);

        return OperateResultExOne.CreateSuccessResult(PackCommand(_PLCCommand));
    }


    private OperateResultExOne<byte[]> BuildWriteCommand(String address, byte[] value, boolean isBit) {
        OperateResultExTwo<OmronFinsDataType, byte[]> analysis = AnalysisAddress(address, isBit);
        if (!analysis.IsSuccess)
            return OperateResultExOne.<byte[]>CreateFailedResult(analysis);

        byte[] _PLCCommand = new byte[8 + value.length];
        _PLCCommand[0] = 0x01;
        _PLCCommand[1] = 0x02;
        if (isBit) {
            _PLCCommand[2] = analysis.Content1.getBitCode();
        } else {
            _PLCCommand[2] = analysis.Content1.getWordCode();
        }

        System.arraycopy(analysis.Content2, 0, _PLCCommand, 3, analysis.Content2.length);

        if (isBit) {
            _PLCCommand[6] = (byte) (value.length / 256);
            _PLCCommand[7] = (byte) (value.length % 256);
        } else {
            _PLCCommand[6] = (byte) (value.length / 2 / 256);
            _PLCCommand[7] = (byte) (value.length / 2 % 256);
        }

        System.arraycopy(value, 0, _PLCCommand, 8, value.length);

        return OperateResultExOne.CreateSuccessResult(PackCommand(_PLCCommand));
    }


    @Override
    protected OperateResult InitializationOnConnect(Socket socket) {

        OperateResultExTwo<byte[], byte[]> read = ReadFromCoreServerBase(socket, handSingle);
        if (!read.IsSuccess)
            return read;

        byte[] buffer = new byte[4];
        buffer[0] = read.Content2[7];
        buffer[1] = read.Content2[6];
        buffer[2] = read.Content2[5];
        buffer[3] = read.Content2[4];
        int status = Utilities.getInt(buffer, 0);
        if (status != 0)
            return new OperateResult(status, GetStatusDescription(status));

        if (read.Content2.length >= 16)
            DA1 = read.Content2[15];

        return OperateResult.CreateSuccessResult();
    }


    @Override
    public OperateResultExOne<byte[]> Read(String address, short length) {

        OperateResultExOne<byte[]> command = BuildReadCommand(address, length, false);
        if (!command.IsSuccess)
            return OperateResultExOne.<byte[]>CreateFailedResult(command);

        OperateResultExOne<byte[]> read = ReadFromCoreServer(command.Content);
        if (!read.IsSuccess)
            return OperateResultExOne.<byte[]>CreateFailedResult(read);


        OperateResultExOne<byte[]> valid = ResponseValidAnalysis(read.Content, true);
        if (!valid.IsSuccess)
            return OperateResultExOne.<byte[]>CreateFailedResult(valid);


        return OperateResultExOne.CreateSuccessResult(valid.Content);
    }


    public OperateResultExOne<boolean[]> ReadBool(String address, short length) {

        OperateResultExOne<byte[]> command = BuildReadCommand(address, length, true);
        if (!command.IsSuccess)
            return OperateResultExOne.<boolean[]>CreateFailedResult(command);

        OperateResultExOne<byte[]> read = ReadFromCoreServer(command.Content);
        if (!read.IsSuccess)
            return OperateResultExOne.<boolean[]>CreateFailedResult(read);

        OperateResultExOne<byte[]> valid = ResponseValidAnalysis(read.Content, true);
        if (!valid.IsSuccess)
            return OperateResultExOne.<boolean[]>CreateFailedResult(valid);

        boolean[] buffer = new boolean[valid.Content.length];
        for (int i = 0; i < valid.Content.length; i++) {
            buffer[i] = valid.Content[i] != 0x00;
        }
        return OperateResultExOne.CreateSuccessResult(buffer);
    }


    public OperateResultExOne<Boolean> ReadBool(String address) {
        OperateResultExOne<boolean[]> read = ReadBool(address, (short) 1);
        if (read.IsSuccess) {
            return OperateResultExOne.CreateSuccessResult(read.Content[0]);
        } else {
            return OperateResultExOne.<Boolean>CreateFailedResult(read);
        }
    }


    @Override
    public OperateResult Write(String address, byte[] value) {

        OperateResultExOne<byte[]> command = BuildWriteCommand(address, value, false);
        if (!command.IsSuccess)
            return command;


        OperateResultExOne<byte[]> read = ReadFromCoreServer(command.Content);
        if (!read.IsSuccess)
            return read;


        OperateResultExOne<byte[]> valid = ResponseValidAnalysis(read.Content, false);
        if (!valid.IsSuccess)
            return valid;


        return OperateResult.CreateSuccessResult();
    }


    public OperateResult Write(String address, boolean value) {
        return Write(address, new boolean[] { value });
    }


    public OperateResult Write(String address, boolean[] values) {
        OperateResult result = new OperateResult();

        byte[] buffer = new byte[values.length];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = values[i] ? (byte) 0x01 : (byte) 0x00;
        }


        OperateResultExOne<byte[]> command = BuildWriteCommand(address, buffer, true);
        if (!command.IsSuccess)
            return command;


        OperateResultExOne<byte[]> read = ReadFromCoreServer(command.Content);
        if (!read.IsSuccess)
            return read;


        OperateResultExOne<byte[]> valid = ResponseValidAnalysis(read.Content, false);
        if (!valid.IsSuccess)
            return valid;


        return OperateResult.CreateSuccessResult();
    }


    // 46494E530000000C0000000000000000000000D6
    private final byte[] handSingle = new byte[] { 0x46, 0x49, 0x4E, 0x53, // FINS
            0x00, 0x00, 0x00, 0x0C,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x01
    };


    @Override
    public String toString() {
        return "OmronFinsNet";
    }


    public static OperateResultExTwo<OmronFinsDataType, byte[]> AnalysisAddress(String address, boolean isBit) {
        OperateResultExTwo<OmronFinsDataType, byte[]> result = new OperateResultExTwo<OmronFinsDataType, byte[]>();
        try {
            switch (address.charAt(0)) {
            case 'D':
            case 'd': {
                // DM
                result.Content1 = OmronFinsDataType.DM;
                break;
            }
            case 'C':
            case 'c': {
                // CIO
                result.Content1 = OmronFinsDataType.CIO;
                break;
            }
            case 'W':
            case 'w': {
                // WR
                result.Content1 = OmronFinsDataType.WR;
                break;
            }
            case 'H':
            case 'h': {
                // HR
                result.Content1 = OmronFinsDataType.HR;
                break;
            }
            case 'A':
            case 'a': {
                // AR
                result.Content1 = OmronFinsDataType.AR;
                break;
            }
            default:
                throw new Exception(StringResources.Language.NotSupportedDataType());
            }

            if (isBit) {

                String[] splits = address.substring(1).split("\\.");
                int addr = Integer.parseInt(splits[0]);
                result.Content2 = new byte[3];
                result.Content2[0] = Utilities.getBytes(addr)[1];
                result.Content2[1] = Utilities.getBytes(addr)[0];

                if (splits.length > 1) {
                    result.Content2[2] = Byte.parseByte(splits[1]);
                    if (result.Content2[2] > 15) {
                        throw new Exception(StringResources.Language.OmronAddressMustBeZeroToFiveteen());
                    }
                }
            } else {

                int addr = Integer.parseInt(address.substring(1));
                result.Content2 = new byte[3];
                result.Content2[0] = Utilities.getBytes(addr)[1];
                result.Content2[1] = Utilities.getBytes(addr)[0];
            }
        } catch (Exception ex) {
            result.Message = ex.getMessage();
            return result;
        }

        result.IsSuccess = true;
        return result;
    }


    public static OperateResultExOne<byte[]> ResponseValidAnalysis(byte[] response, boolean isRead) {

        if (response.length >= 16) {

            byte[] buffer = new byte[4];
            buffer[0] = response[15];
            buffer[1] = response[14];
            buffer[2] = response[13];
            buffer[3] = response[12];

            int err = Utilities.getInt(buffer, 0);
            if (err > 0)
                return new OperateResultExOne<byte[]>(err, GetStatusDescription(err));

            if (response.length >= 30) {
                err = response[28] * 256 + response[29];
                if (err > 0)
                    return new OperateResultExOne<byte[]>(err, StringResources.Language.OmronReceiveDataError());

                if (!isRead) {

                    return OperateResultExOne.CreateSuccessResult(new byte[0]);
                } else {

                    byte[] content = new byte[response.length - 30];
                    if (content.length > 0) {
                        System.arraycopy(response, 30, content, 0, content.length);
                    }
                    return OperateResultExOne.CreateSuccessResult(content);
                }
            }
        }

        return new OperateResultExOne<byte[]>(StringResources.Language.OmronReceiveDataError());
    }

    public static String GetStatusDescription(int err) {
        switch (err) {
        case 0:
            return StringResources.Language.OmronStatus0();
        case 1:
            return StringResources.Language.OmronStatus1();
        case 2:
            return StringResources.Language.OmronStatus2();
        case 3:
            return StringResources.Language.OmronStatus3();
        case 20:
            return StringResources.Language.OmronStatus20();
        case 21:
            return StringResources.Language.OmronStatus21();
        case 22:
            return StringResources.Language.OmronStatus22();
        case 23:
            return StringResources.Language.OmronStatus23();
        case 24:
            return StringResources.Language.OmronStatus24();
        case 25:
            return StringResources.Language.OmronStatus25();
        default:
            return StringResources.Language.UnknownError();
        }
    }
}
