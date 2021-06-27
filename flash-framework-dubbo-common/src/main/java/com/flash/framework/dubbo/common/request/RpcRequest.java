package com.flash.framework.dubbo.common.request;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("租户ID")
    private Integer tenantId;

    /**
     * 调用人
     */
    @ApiModelProperty("调用人")
    private String operator;

    /**
     * 参数校验，可以进行复杂参数/复杂逻辑的校验
     *
     * @throws IllegalArgumentException
     */
    public void checkRequestParam() throws IllegalArgumentException {

    }
}