package com.zw.Model;

public class OutPutJson<T> {
    private String state;   //说明
    private String code;    //状态码
    private T date;   //这个就是我们实际的数据

    public OutPutJson(String state, String code, T date) {
        this.state = state;
        this.code = code;
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }
}
