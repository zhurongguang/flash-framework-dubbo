package com.flash.framework.dubbo.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * dubbo 响应实体
 *
 * @author zhurg
 * @date 2019/2/11 - 下午5:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 6682190698916789412L;

    /**
     * 调用是否成功
     */
    private boolean success;

    /**
     * 响应结果
     */
    private T result;

    /**
     * 错误码
     */
    private String code;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 返回成功
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> ok(T result) {
        return new RpcResponse<T>().setResult(result).setSuccess(true);
    }

    /**
     * 返回失败
     *
     * @param result
     * @param errorMsg
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> fail(T result, String errorMsg) {
        return new RpcResponse<T>().setResult(result).setErrorMsg(errorMsg).setSuccess(false);
    }

    /**
     * 返回失败
     *
     * @param errorMsg
     * @return
     */
    public static <T> RpcResponse<T> fail(String errorMsg) {
        return new RpcResponse<T>().setErrorMsg(errorMsg).setSuccess(false);
    }

    /**
     * 返回失败
     *
     * @param code
     * @param result
     * @param errorMsg
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> fail(T result, String code, String errorMsg) {
        return new RpcResponse<T>().setResult(result).setCode(code).setErrorMsg(errorMsg).setSuccess(false);
    }

    /**
     * 返回失败
     *
     * @param code
     * @param errorMsg
     * @return
     */
    public static RpcResponse<Void> fail(String code, String errorMsg) {
        return new RpcResponse<Void>().setCode(code).setErrorMsg(errorMsg).setSuccess(false);
    }
}