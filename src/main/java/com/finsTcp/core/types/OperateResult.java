package com.finsTcp.core.types;

import com.finsTcp.StringResources;


public class OperateResult {



    public OperateResult(){

    }


    public OperateResult(String msg){
        this.Message = msg;
    }


    public OperateResult(int err,String msg){
        this.ErrorCode = err;
        this.Message = msg;
    }



    public boolean IsSuccess = false;



    public String Message = StringResources.Language.UnknownError();



    public int ErrorCode = 10000;



    public String ToMessageShowString() {
        return "错误代码：" + ErrorCode + "\r\n错误信息：" + Message;
    }



    public void CopyErrorFromOther(OperateResult result) {
        if (result != null) {
            ErrorCode = result.ErrorCode;
            Message = result.Message;
        }

    }


    public static OperateResult CreateSuccessResult() {
        OperateResult result = new OperateResult();
        result.IsSuccess = true;
        result.Message = "success";
        return result;
    }

}

