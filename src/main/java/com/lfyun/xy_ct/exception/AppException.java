package com.lfyun.xy_ct.exception;

import com.lfyun.xy_ct.common.enums.ExceptionCodeEnums;

import lombok.Getter;

/**
 * Created by Administrator on 2017/10/15 0015.
 */
@Getter
public class AppException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5381161989048302081L;
	/**错误状态码.*/
    private Integer code;

    public AppException(ExceptionCodeEnums exceptionCodeEnums) {
        super(exceptionCodeEnums.getMsg());
        this.code = exceptionCodeEnums.getCode();
    }

    public AppException(ExceptionCodeEnums exceptionCodeEnums , String msg) {
        super(msg);
        this.code = exceptionCodeEnums.getCode();
    }
}
