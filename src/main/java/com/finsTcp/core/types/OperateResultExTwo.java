package com.finsTcp.core.types;


public class OperateResultExTwo<T1,T2> extends OperateResult
{


    public OperateResultExTwo(){
        super();
    }


    public OperateResultExTwo(String msg){
        super(msg);
    }


    public OperateResultExTwo(int err,String msg){
        super(err,msg);
    }

    public T1 Content1 = null;



    public T2 Content2 = null;


    public static <T1,T2> OperateResultExTwo<T1,T2> CreateSuccessResult(T1 content1,T2 content2){
        OperateResultExTwo<T1,T2> result = new OperateResultExTwo<T1,T2>();
        result.IsSuccess = true;
        result.Content1 = content1;
        result.Content2 = content2;
        result.Message = "success";
        return result;
    }


    public static <T1,T2> OperateResultExTwo<T1,T2> CreateFailedResult(OperateResult result){
        OperateResultExTwo resultExTwo = new OperateResultExTwo();
        resultExTwo.CopyErrorFromOther(result);
        return resultExTwo;
    }

}
