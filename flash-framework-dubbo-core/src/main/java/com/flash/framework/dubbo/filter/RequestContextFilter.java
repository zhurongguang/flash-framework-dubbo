package com.flash.framework.dubbo.filter;

import com.flash.framework.dubbo.common.request.RpcRequest;
import com.flash.framework.dubbo.context.RpcRequestContext;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Objects;

/**
 * @author zhurg
 * @date 2021/6/27 - 上午9:03
 */
@Activate(
        group = {CommonConstants.PROVIDER}, order = 100
)
public class RequestContextFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Object[] args = invocation.getArguments();
            if (Objects.nonNull(args) && args.length == 1 && args[0] instanceof RpcRequest) {
                RpcRequest request = (RpcRequest) args[0];
                RpcRequestContext.set(request.getTenantId(), request.getOperator());
            }
            return invoker.invoke(invocation);
        } finally {
            RpcRequestContext.clear();
        }
    }
}