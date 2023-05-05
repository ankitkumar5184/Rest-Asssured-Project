package com.connecture.utils.exception;



public class ConnectureCustomException extends Exception{

    private static final long serialVersionUID = 1L;

    public ConnectureCustomException(Exception e){
        super(e);
    }

    public ConnectureCustomException(String msg){
        super(msg);
    }

}
