package com.lfyun.xy_ct.common.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Getter
public enum ResultCodeEnums {
    OK(200 , "成功"),
    ERROR(500 , "失败")
    ;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态码信息
     */
    private String msg;

    ResultCodeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
