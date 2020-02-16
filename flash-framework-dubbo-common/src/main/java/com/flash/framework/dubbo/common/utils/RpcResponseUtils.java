package com.flash.framework.dubbo.common.utils;

import com.flash.framework.dubbo.common.response.RpcResponse;

import java.util.function.Function;

/**
 * @author zhurg
 * @date 2019/10/14 - 上午10:04
 */
public class RpcResponseUtils {

    public static <T> T getResponse(RpcResponse<T> response, Function<String, Exception> function) throws Exception {
        if (response.isSuccess()) {
            return response.getResult();
        } else {
            throw function.apply(response.getErrorMsg());
        }
    }
}