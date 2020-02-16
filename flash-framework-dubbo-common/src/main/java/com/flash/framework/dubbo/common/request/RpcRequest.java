package com.flash.framework.dubbo.common.request;

import lombok.Data;

import java.io.Serializable;

/**
 * dubbo请求
 *
 * @author zhurg
 * @date 2019/2/11 - 下午5:24
 */
@Data
public abstract class RpcRequest implements Serializable {

    private static final long serialVersionUID = 1185949783796005427L;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 参数校验，可以进行复杂参数/复杂逻辑的校验
     *
     * @throws IllegalArgumentException
     */
    public void checkRequestParam() throws IllegalArgumentException {

    }
}