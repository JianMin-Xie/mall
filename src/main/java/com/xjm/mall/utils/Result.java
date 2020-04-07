package com.xjm.mall.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 返还给前端的结果
 * @author Jm
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -6094032810522860950L;
    //响应码 200为成功
    private int resultCode;
    //响应msg
    private String message;
    //返回数据
    private T data;

}
