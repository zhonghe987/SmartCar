package com.FinsTCP.core.types;



public class OperateResultExOne<T> extends  OperateResult
{


    public OperateResultExOne(){
        super();
    }


    public OperateResultExOne(String msg){
        super(msg);
    }


    public OperateResultExOne(int err,String msg){
        super(err,msg);
    }



    public T Content = null;


    public static <T> OperateResultExOne<T> CreateFailedResult(OperateResult result){
        OperateResultExOne<T> resultExOne = new OperateResultExOne<T>();
        resultExOne.CopyErrorFromOther(result);
        return resultExOne;
    }



    public static <T> OperateResultExOne<T> CreateSuccessResult(T content){
        OperateResultExOne<T> result = new OperateResultExOne<T>();
        result.IsSuccess = true;
        result.Content = content;
        result.Message = "success";
        return result;
    }

}
