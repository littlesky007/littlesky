package com.littlesky.rpc.servlet;

public class ResponseInfo
{
    private int flage;

    private String returnInfo;

    private String errorInfo;

    public int getFlage() {
        return flage;
    }

    public void setFlage(int flage) {
        this.flage = flage;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
