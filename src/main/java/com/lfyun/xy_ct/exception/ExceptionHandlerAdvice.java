package com.lfyun.xy_ct.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import com.lfyun.xy_ct.common.Result;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
	
    @ExceptionHandler(value = { Throwable.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleOtherExceptions(final Exception ex, final ServletWebRequest req) {
    	Result<Void> result = null;
    	if(ex instanceof AppException) {
    		AppException appException = (AppException) ex;
    		LOGGER.error("code:{} message:{}", appException.getCode(), appException.getMessage());
    		result = Result.<Void>fail(appException.getCode()).setMessage(appException.getMessage());
    	} else {
    		LOGGER.error("系统内部异常:" + req, ex);
        	result = Result.<Void>fail().setMessage("系统内部异常");
    	}
        return result;
    }
}
